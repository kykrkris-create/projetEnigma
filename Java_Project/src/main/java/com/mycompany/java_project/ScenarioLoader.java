/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project; 
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/** 
 * @author Rania
 */
public class ScenarioLoader { 
    public Scenario charger(File dossierScenario) throws IOException {
        File manifest = new File(dossierScenario, "manifest.json");
        if (!manifest.exists()) {
            throw new IllegalArgumentException("Fichier manifest.json absent");
        }
        File dossierImages = new File(dossierScenario, "images");
        if (!dossierImages.exists()) {
            throw new IllegalArgumentException("Dossier images/ absent");
        } 
        String contenu = Files.readString(manifest.toPath());
        JSONObject json = new JSONObject(contenu);
        if (json.getInt("schemaVersion") != 1) {
            throw new IllegalArgumentException("schemaVersion inconnu");
        }
        String id = json.getString("id");
        String title = json.getString("title");
        String author = json.getString("author");
        String start = json.getString("start");
        JSONObject puzzlesJson = json.getJSONObject("puzzles");
        Map<String, Puzzle> puzzles = new HashMap<>(); 
        for (String puzzleId : puzzlesJson.keySet()) {
            JSONObject pJson = puzzlesJson.getJSONObject(puzzleId);
            String type = pJson.getString("type");
            String prompt = pJson.getString("prompt");
            String image = pJson.optString("image", null); 
            if (image != null) {
                File img = new File(dossierScenario, image);
                if (!img.exists()) {
                    
                    throw new IllegalArgumentException("Image introuvable : " + image);
                }
            } 
            List<String> choices = new ArrayList<>();
            if (pJson.has("choices")) {
                JSONArray arr = pJson.getJSONArray("choices");
                for (int i = 0; i < arr.length(); i++) {
                    choices.add(arr.getString(i));
                }
            }

            Map<String, String> routes = new HashMap<>();
            if (!pJson.has("routes")) {
                throw new IllegalArgumentException("Routes manquantes pour : " + puzzleId);
            }
            JSONObject rJson = pJson.getJSONObject("routes");
for (String key : rJson.keySet()) {
    routes.put(key, rJson.getString(key));
} 
//Pour un choix aleatoire
if (pJson.optBoolean("shuffle", false) && !choices.isEmpty()) {
    List<String> destinations = new ArrayList<>();
    for (String choix : choices) {
        if (routes.containsKey(choix)) {
            destinations.add(routes.get(choix));
        }
    }
    Collections.shuffle(destinations);
    for (int i = 0; i < choices.size(); i++) {
        routes.put(choices.get(i), destinations.get(i));
    }
} 
for (String dest : routes.values()) {
                if (!dest.startsWith("end_") && !puzzlesJson.has(dest)) {
                    throw new IllegalArgumentException(
                        "Route invalide : " + puzzleId + " -> " + dest);
                    
                }
            } 
            Set<String> typesValides = new HashSet<>();
            typesValides.add("qcm");
            typesValides.add("text");
            typesValides.add("boolean");
            if (!typesValides.contains(type)) {
                
                
                throw new IllegalArgumentException("Type non supporté : " + type);
            } 
            puzzles.put(puzzleId, new Puzzle(puzzleId, type, prompt, image, choices, routes));
        } 
        if (!puzzles.containsKey(start)) {
            throw new IllegalArgumentException("Énigme de départ introuvable : " + start);
        }

        return new Scenario(id, title, author, start, puzzles, dossierScenario.getAbsolutePath());
    }
}
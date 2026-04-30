/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import java.util.List;
import java.util.Map;

/** 
 * Partie du scenario
 * @author Rania
 */
public class Puzzle { 
    private String id;
    private String type;
    private String prompt;
    private String image;
    private List<String> choices;
    private Map<String, String> routes;
    public Puzzle(String id, String type, String prompt, String image, List<String> choices, Map<String, String> routes) {
        this.id = id;
        this.type = type;
        this.prompt = prompt;
        this.image = image;
        this.choices = choices;
        this.routes = routes;
    }
    public String getId() { 
        return id; 
    }
    public String getType() { 
        return type; 
    }
    public String getPrompt() { 
        return prompt;
    }
    public String getImage() {
        return image;
    }
    public List<String> getChoices() { 
        return choices;
    }
    public Map<String, String> getRoutes() {
        return routes; 
    } 
    public String getNextPuzzleId(String reponse) {
        if (routes.containsKey(reponse)) {
            return routes.get(reponse);
            
            
        }
        return routes.get("*");
    }
}
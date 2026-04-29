/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import java.util.Map;

/**
 * Représente un scénario complet.
 * @author Rania
 */
public class Scenario {

    private String id;
    private String title;
    private String author;
    private String startPuzzleId;
    private Map<String, Puzzle> puzzles;
    private String dossierScenario;

    public Scenario(String id, String title, String author,
                    String startPuzzleId, Map<String, Puzzle> puzzles,
                    String dossierScenario) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.startPuzzleId = startPuzzleId;
        this.puzzles = puzzles;
        this.dossierScenario = dossierScenario;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getStartPuzzleId() { return startPuzzleId; }
    public String getDossierScenario() { return dossierScenario; }

    public Puzzle getPuzzle(String id) {
        return puzzles.get(id);
    }
}
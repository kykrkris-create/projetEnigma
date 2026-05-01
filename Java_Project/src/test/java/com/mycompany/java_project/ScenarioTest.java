/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.java_project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/** 
 * @author Rania
 */
public class ScenarioTest {

    @Test
    public void testCreationScenario() {
        Map<String, Puzzle> puzzles = new HashMap<>();
        Puzzle p1 = new Puzzle("p1", "qcm", "?", "img.png",
            Collections.emptyList(), new HashMap<>());
        puzzles.put("p1", p1);

        Scenario s = new Scenario("test_id", "Titre", "Auteur", "p1", puzzles, "/chemin");

        assertEquals("test_id", s.getId());
        assertEquals("Titre", s.getTitle());
        assertEquals("Auteur", s.getAuthor());
        assertEquals("p1", s.getStartPuzzleId());
        assertEquals("/chemin", s.getDossierScenario());
    }

    @Test
    public void testGetPuzzle_Existant() {
        Map<String, Puzzle> puzzles = new HashMap<>();
        Puzzle p1 = new Puzzle("p1", "qcm", "?", "img.png",
            Collections.emptyList(), new HashMap<>());
        puzzles.put("p1", p1);

        Scenario s = new Scenario("id", "t", "a", "p1", puzzles, "/c");

        assertSame(p1, s.getPuzzle("p1"));
    }

    @Test
    public void testGetPuzzle_Inexistant() {
        Scenario s = new Scenario("id", "t", "a", "p1", new HashMap<>(), "/c");
        assertNull(s.getPuzzle("p99"));
    }
}
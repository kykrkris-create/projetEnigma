/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.java_project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/** 
 * @author X515
 */
public class ProgressionTest { 
        private Scenario chargerScenarioReel() throws Exception {
        File dossier = new File("scenarios/maison_hantee");
        ScenarioLoader loader = new ScenarioLoader();
        return loader.charger(dossier);
    }

    @Test
    public void testCheminVictoire() throws Exception {
        Scenario s = chargerScenarioReel();

        // p1 : clé rouillée → p2
        Puzzle p1 = s.getPuzzle("p1");
        assertEquals("p2", p1.getNextPuzzleId("clé rouillée"));

        // p2 : Billy Loomis → p3
        Puzzle p2 = s.getPuzzle("p2");
        assertEquals("p3", p2.getNextPuzzleId("Billy Loomis"));

        // p3 : MEURTRE → p4
        Puzzle p3 = s.getPuzzle("p3");
        assertEquals("p4", p3.getNextPuzzleId("MEURTRE"));

        // p4 : ENTERREMENT → p5
        Puzzle p4 = s.getPuzzle("p4");
        assertEquals("p5", p4.getNextPuzzleId("ENTERREMENT"));

        // p5 : au moins une route mène à end_win
        Puzzle p5 = s.getPuzzle("p5");
        assertTrue(p5.getRoutes().containsValue("end_win"));
    }

    @Test
    public void testCheminCleRouilleeCorrecte() throws Exception {
        Scenario s = chargerScenarioReel();
        Puzzle p1 = s.getPuzzle("p1");
        assertNotEquals("end_lose", p1.getNextPuzzleId("clé rouillée"));
        assertNotEquals("end_lose_pi", p1.getNextPuzzleId("clé rouillée"));
    }

    @Test
    public void testCheminCleMoisieMeneAP1bis() throws Exception {
        Scenario s = chargerScenarioReel();
        Puzzle p1 = s.getPuzzle("p1");
        assertEquals("p1bis", p1.getNextPuzzleId("clé moisie"));
    }

    @Test
    public void testMauvaiseReponseScreamMene_AFin() throws Exception {
        Scenario s = chargerScenarioReel();
        Puzzle p2 = s.getPuzzle("p2");
        String dest = p2.getNextPuzzleId("Roman Bridger");
        assertTrue(dest.startsWith("end_"));
    }

    @Test
    public void testDemarrage() throws Exception {
        Scenario s = chargerScenarioReel();
        assertEquals("p1", s.getStartPuzzleId());
        assertNotNull(s.getPuzzle(s.getStartPuzzleId()));
    }

    @Test
    public void testNombreEnigmes() throws Exception {
        Scenario s = chargerScenarioReel();
        // Le scénario doit avoir au moins 3 énigmes (exigence du prof, section 1.3.2)
        int count = 0;
        for (String id : new String[]{"p1", "p1bis", "p2", "p3", "p4", "p5"}) {
            if (s.getPuzzle(id) != null) count++;
        }
        assertTrue(count >= 3, "Le scénario doit contenir au moins 3 énigmes");
    }
}

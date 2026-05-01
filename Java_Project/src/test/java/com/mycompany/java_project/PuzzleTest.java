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
public class PuzzleTest {

    @Test
    public void testGetters() {
        Map<String, String> routes = new HashMap<>();
        routes.put("Billy Loomis", "p3");
        routes.put("*", "end_lose");
        List<String> choices = Arrays.asList("Roman", "Billy Loomis", "Mickey");

        Puzzle p = new Puzzle("p2", "qcm", "Question ?", "img.png", choices, routes);

        assertEquals("p2", p.getId());
        assertEquals("qcm", p.getType());
        assertEquals("Question ?", p.getPrompt());
        assertEquals("img.png", p.getImage());
        assertEquals(choices, p.getChoices());
        assertEquals(routes, p.getRoutes());
    }

    @Test
    public void testGetNextPuzzleId_RouteExistante() {
        Map<String, String> routes = new HashMap<>();
        routes.put("Billy Loomis", "p3");
        routes.put("*", "end_lose");

        Puzzle p = new Puzzle("p2", "qcm", "?", "img.png",
            Collections.emptyList(), routes);

        assertEquals("p3", p.getNextPuzzleId("Billy Loomis"));
    }

    @Test
    public void testGetNextPuzzleId_RouteParDefaut() {
        Map<String, String> routes = new HashMap<>();
        routes.put("Billy Loomis", "p3");
        routes.put("*", "end_lose");

        Puzzle p = new Puzzle("p2", "qcm", "?", "img.png",
            Collections.emptyList(), routes);

        assertEquals("end_lose", p.getNextPuzzleId("mauvaise réponse"));
    }

    @Test
    public void testGetNextPuzzleId_AucuneRoute() {
        Map<String, String> routes = new HashMap<>();
        routes.put("Billy Loomis", "p3");

        Puzzle p = new Puzzle("p2", "qcm", "?", "img.png",
            Collections.emptyList(), routes);

        assertNull(p.getNextPuzzleId("autre chose"));
    }
}
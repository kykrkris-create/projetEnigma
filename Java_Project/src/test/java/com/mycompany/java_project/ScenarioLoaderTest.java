/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.java_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** 
 * @author Rania
 */
public class ScenarioLoaderTest {

    private Path dossierTemp;
    private Path dossierImages;
    private ScenarioLoader loader;

    @BeforeEach
    public void setUp() throws IOException {
        // Crée un dossier temporaire pour chaque test
        dossierTemp = Files.createTempDirectory("scenario_test");
        dossierImages = dossierTemp.resolve("images");
        Files.createDirectory(dossierImages);
        loader = new ScenarioLoader();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Nettoie après chaque test
        Files.walk(dossierTemp)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(p -> p.toFile().delete());
    }

    /** Crée un fichier image vide pour les tests. */
    private void creerImage(String nom) throws IOException {
        Files.createFile(dossierImages.resolve(nom));
    }

    /** Écrit le manifest.json dans le dossier temporaire. */
    private void ecrireManifest(String contenu) throws IOException {
        Files.writeString(dossierTemp.resolve("manifest.json"), contenu);
    }

    @Test
    public void testChargerScenarioValide() throws IOException {
        creerImage("img1.png");
        ecrireManifest("""
            {
                "schemaVersion": 1,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "qcm",
                        "prompt": "Q ?",
                        "image": "images/img1.png",
                        "choices": ["A", "B"],
                        "routes": {"A": "end_win", "B": "end_lose"}
                    }
                }
            }
            """);

        Scenario s = loader.charger(dossierTemp.toFile());
        assertNotNull(s);
        assertEquals("test", s.getId());
        assertEquals("p1", s.getStartPuzzleId());
        assertNotNull(s.getPuzzle("p1"));
    }

    @Test
    public void testRejetManifestAbsent() {
        // Pas de manifest.json créé
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
        assertTrue(ex.getMessage().contains("manifest.json"));
    }

    @Test
    public void testRejetSchemaVersionInconnue() throws IOException {
        creerImage("img1.png");
        ecrireManifest("""
            {
                "schemaVersion": 99,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "qcm",
                        "prompt": "?",
                        "image": "images/img1.png",
                        "choices": ["A"],
                        "routes": {"A": "end_win"}
                    }
                }
            }
            """);

        assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
    }

    @Test
    public void testRejetImageManquante() throws IOException {
        // On NE crée PAS l'image
        ecrireManifest("""
            {
                "schemaVersion": 1,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "qcm",
                        "prompt": "?",
                        "image": "images/manquante.png",
                        "choices": ["A"],
                        "routes": {"A": "end_win"}
                    }
                }
            }
            """);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
        assertTrue(ex.getMessage().toLowerCase().contains("image"));
    }

    @Test
    public void testRejetTypeInconnu() throws IOException {
        creerImage("img1.png");
        ecrireManifest("""
            {
                "schemaVersion": 1,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "slide",
                        "prompt": "?",
                        "image": "images/img1.png",
                        "routes": {"A": "end_win"}
                    }
                }
            }
            """);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
        assertTrue(ex.getMessage().contains("Type"));
    }

    @Test
    public void testRejetRouteVersEnigmeInexistante() throws IOException {
        creerImage("img1.png");
        ecrireManifest("""
            {
                "schemaVersion": 1,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "qcm",
                        "prompt": "?",
                        "image": "images/img1.png",
                        "choices": ["A"],
                        "routes": {"A": "p99"}
                    }
                }
            }
            """);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
        assertTrue(ex.getMessage().contains("Route"));
    }

    @Test
    public void testRejetRoutesManquantes() throws IOException {
        creerImage("img1.png");
        ecrireManifest("""
            {
                "schemaVersion": 1,
                "id": "test",
                "title": "Test",
                "author": "Rania",
                "start": "p1",
                "puzzles": {
                    "p1": {
                        "type": "qcm",
                        "prompt": "?",
                        "image": "images/img1.png",
                        "choices": ["A"]
                    }
                }
            }
            """);

        assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossierTemp.toFile()));
    }
}
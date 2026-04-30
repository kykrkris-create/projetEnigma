package com.mycompany.java_project;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.JFrame;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour {@link ScenarioLoader} et {@link Moteur}.
 *
 * <p>Les tests du ScenarioLoader vérifient :
 * <ul>
 *   <li>Le chargement d'un scénario valide.</li>
 *   <li>Le rejet de scénarios invalides (manifest absent, image manquante, etc.).</li>
 * </ul>
 *
 * <p>Les tests du Moteur vérifient :
 * <ul>
 *   <li>Le démarrage sur la première énigme.</li>
 *   <li>La progression correcte via onReponse.</li>
 *   <li>La détection de fin de partie (victoire / défaite).</li>
 * </ul>
 *
 * @author Lou-Ann
 */
class TestScenarioLoaderEtMoteur {

    // -----------------------------------------------------------------------
    // Utilitaires de création de scénarios temporaires pour les tests
    // -----------------------------------------------------------------------

    /**
     * Crée un dossier de scénario temporaire valide contenant :
     * un manifest.json correct et un dossier images/ avec une image factice.
     *
     * @return le dossier racine du scénario temporaire.
     * @throws IOException en cas d'erreur d'écriture.
     */
    private File creerScenarioValide() throws IOException {
        Path dossier = Files.createTempDirectory("scenario_test");
        Path images = dossier.resolve("images");
        Files.createDirectories(images);

        // Image factice (1 octet suffit, on teste seulement l'existence)
        Files.write(images.resolve("enigme1.png"), new byte[]{0});
        Files.write(images.resolve("enigme2.png"), new byte[]{0});

        String manifest = """
            {
              "schemaVersion": 1,
              "id": "test_valide",
              "title": "Scénario de test",
              "author": "JUnit",
              "start": "p1",
              "puzzles": {
                "p1": {
                  "type": "boolean",
                  "prompt": "Vrai ou faux ?",
                  "image": "images/enigme1.png",
                  "routes": {
                    "true":  "p2",
                    "false": "end_lose"
                  }
                },
                "p2": {
                  "type": "qcm",
                  "prompt": "Choisissez",
                  "image": "images/enigme2.png",
                  "choices": ["A", "B"],
                  "routes": {
                    "A": "end_win",
                    "B": "end_lose"
                  }
                }
              }
            }
            """;
        Files.writeString(dossier.resolve("manifest.json"), manifest);
        return dossier.toFile();
    }

    // -----------------------------------------------------------------------
    //  TESTS — ScenarioLoader
    // -----------------------------------------------------------------------

    /**
     * Un scénario complet et correct doit être chargé sans exception.
     * Les métadonnées (id, title, auteur, start) doivent correspondre au manifest.
     */
    @Test
    void testChargementScenarioValide() throws Exception {
        File dossier = creerScenarioValide();
        ScenarioLoader loader = new ScenarioLoader();

        Scenario scenario = loader.charger(dossier);

        assertNotNull(scenario, "Le scénario ne doit pas être null");
        assertEquals("test_valide", scenario.getId());
        assertEquals("Scénario de test", scenario.getTitle());
        assertEquals("JUnit", scenario.getAuthor());
        assertEquals("p1", scenario.getStartPuzzleId());
        assertNotNull(scenario.getPuzzle("p1"), "L'énigme p1 doit exister");
        assertNotNull(scenario.getPuzzle("p2"), "L'énigme p2 doit exister");
    }

    /**
     * Un dossier sans manifest.json doit lever une IllegalArgumentException
     * avec un message qui mentionne "manifest".
     */
    @Test
    void testManifestAbsent() throws Exception {
        Path dossier = Files.createTempDirectory("scenario_sans_manifest");
        Files.createDirectories(dossier.resolve("images"));

        ScenarioLoader loader = new ScenarioLoader();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> loader.charger(dossier.toFile())
        );
        assertTrue(ex.getMessage().toLowerCase().contains("manifest"),
            "Le message d'erreur doit mentionner 'manifest'");
    }

    /**
     * Un scénario référençant une image absente du dossier images/
     * doit lever une IllegalArgumentException mentionnant l'image introuvable.
     */
    @Test
    void testImageAbsente() throws Exception {
        Path dossier = Files.createTempDirectory("scenario_image_absente");
        Files.createDirectories(dossier.resolve("images"));
        // On ne crée PAS l'image enigme_fantome.png

        String manifest = """
            {
              "schemaVersion": 1,
              "id": "test_image_absente",
              "title": "Test",
              "author": "JUnit",
              "start": "p1",
              "puzzles": {
                "p1": {
                  "type": "text",
                  "prompt": "Question",
                  "image": "images/enigme_fantome.png",
                  "routes": { "*": "end_lose" }
                }
              }
            }
            """;
        Files.writeString(dossier.resolve("manifest.json"), manifest);

        ScenarioLoader loader = new ScenarioLoader();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> loader.charger(dossier.toFile())
        );
        assertTrue(ex.getMessage().toLowerCase().contains("image"),
            "Le message doit mentionner 'image'");
    }

    /**
     * Un scénario avec schemaVersion != 1 doit être rejeté.
     */
    @Test
    void testSchemaVersionInconnue() throws Exception {
        Path dossier = Files.createTempDirectory("scenario_version_inconnue");
        Files.createDirectories(dossier.resolve("images"));

        String manifest = """
            {
              "schemaVersion": 99,
              "id": "test",
              "title": "Test",
              "author": "JUnit",
              "start": "p1",
              "puzzles": {
                "p1": {
                  "type": "text",
                  "prompt": "?",
                  "routes": { "*": "end_lose" }
                }
              }
            }
            """;
        Files.writeString(dossier.resolve("manifest.json"), manifest);

        ScenarioLoader loader = new ScenarioLoader();
        assertThrows(IllegalArgumentException.class,
            () -> loader.charger(dossier.toFile()),
            "Une schemaVersion inconnue doit être rejetée");
    }

    /**
     * Une route pointant vers une énigme inexistante (ex: "p99") doit
     * être rejetée lors du chargement.
     */
    @Test
    void testRouteVersEnigmeInexistante() throws Exception {
        Path dossier = Files.createTempDirectory("scenario_route_invalide");
        Files.createDirectories(dossier.resolve("images"));
        Files.write(dossier.resolve("images").resolve("img.png"), new byte[]{0});

        String manifest = """
            {
              "schemaVersion": 1,
              "id": "test",
              "title": "Test",
              "author": "JUnit",
              "start": "p1",
              "puzzles": {
                "p1": {
                  "type": "text",
                  "prompt": "?",
                  "image": "images/img.png",
                  "routes": { "*": "p99" }
                }
              }
            }
            """;
        Files.writeString(dossier.resolve("manifest.json"), manifest);

        ScenarioLoader loader = new ScenarioLoader();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> loader.charger(dossier.toFile())
        );
        assertTrue(ex.getMessage().contains("p99"),
            "Le message doit indiquer l'identifiant invalide (p99)");
    }

    /**
     * Un type d'énigme inconnu du moteur (ex: "slide") doit être rejeté.
     */
    @Test
    void testTypeEnigmeNonSupporte() throws Exception {
        Path dossier = Files.createTempDirectory("scenario_type_inconnu");
        Files.createDirectories(dossier.resolve("images"));
        Files.write(dossier.resolve("images").resolve("img.png"), new byte[]{0});

        String manifest = """
            {
              "schemaVersion": 1,
              "id": "test",
              "title": "Test",
              "author": "JUnit",
              "start": "p1",
              "puzzles": {
                "p1": {
                  "type": "slide",
                  "prompt": "?",
                  "image": "images/img.png",
                  "routes": { "*": "end_lose" }
                }
              }
            }
            """;
        Files.writeString(dossier.resolve("manifest.json"), manifest);

        ScenarioLoader loader = new ScenarioLoader();
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> loader.charger(dossier.toFile())
        );
        assertTrue(ex.getMessage().toLowerCase().contains("slide"),
            "Le message doit mentionner le type non supporté");
    }

    // -----------------------------------------------------------------------
    //  TESTS — Moteur
    //  On utilise un faux JFrame (headless) pour éviter d'ouvrir une fenêtre.
    // -----------------------------------------------------------------------

    /**
     * Construit un scénario en mémoire (sans fichier) pour tester le Moteur.
     * Parcours : p1 --(oui)--> p2 --(ok)--> end_win
     *                               \--(ko)--> end_lose
     */
    private Scenario creerScenarioEnMemoire() {
        Map<String, String> routesP1 = new HashMap<>();
        routesP1.put("oui", "p2");
        routesP1.put("*",   "end_lose");

        Map<String, String> routesP2 = new HashMap<>();
        routesP2.put("ok",  "end_win");
        routesP2.put("ko",  "end_lose");

        Puzzle p1 = new Puzzle("p1", "text",    "Q1", null, List.of(), routesP1);
        Puzzle p2 = new Puzzle("p2", "boolean", "Q2", null, List.of("ok", "ko"), routesP2);

        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put("p1", p1);
        puzzles.put("p2", p2);

        return new Scenario("id_test", "Test", "JUnit", "p1", puzzles, "/tmp");
    }

    /**
     * Après demarrer(), le moteur doit afficher la première énigme (p1).
     * On vérifie simplement qu'aucune exception n'est lancée lors du démarrage.
     */
    @Test
    void testDemarrageSansException() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);

        assertDoesNotThrow(moteur::demarrer,
            "demarrer() ne doit pas lever d'exception sur un scénario valide");
        fenetre.dispose();
    }

    /**
     * Une bonne réponse à p1 ("oui") doit faire passer le moteur à p2
     * sans exception.
     */
    @Test
    void testProgressionBonneReponse() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);
        moteur.demarrer();

        assertDoesNotThrow(() -> moteur.onReponse("oui"),
            "Une bonne réponse doit avancer sans exception");
        fenetre.dispose();
    }

    /**
     * Une mauvaise réponse sans route * définie ne doit pas propager d'exception
     * silencieuse : le moteur doit l'absorber et afficher un message (testé
     * en vérifiant l'absence d'exception non attrapée).
     */
    @Test
    void testProgressionMauvaiseReponseAvecEtoile() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);
        moteur.demarrer();

        // La route * redirige vers end_lose — ne doit pas planter
        assertDoesNotThrow(() -> moteur.onReponse("reponse_inconnue"),
            "La route * doit absorber toute réponse non reconnue");
        fenetre.dispose();
    }

    /**
     * Parcours complet victoire : p1("oui") → p2("ok") → end_win.
     * Vérifie que le moteur traverse tout le scénario sans erreur.
     */
    @Test
    void testParcoursCompletVictoire() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);
        moteur.demarrer();

        assertDoesNotThrow(() -> {
            moteur.onReponse("oui"); // p1 → p2
            moteur.onReponse("ok");  // p2 → end_win
        }, "Le parcours complet jusqu'à la victoire ne doit pas lever d'exception");
        fenetre.dispose();
    }

    /**
     * Parcours complet défaite : p1("non") → end_lose via route *.
     */
    @Test
    void testParcoursCompletDefaite() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);
        moteur.demarrer();

        assertDoesNotThrow(() -> moteur.onReponse("non"),
            "Un parcours vers end_lose ne doit pas lever d'exception");
        fenetre.dispose();
    }

    // -----------------------------------------------------------------------
    //  TESTS — Puzzle.getNextPuzzleId  (logique de routage)
    // -----------------------------------------------------------------------

    /**
     * getNextPuzzleId doit retourner la destination exacte quand la clé existe.
     */
    @Test
    void testRoutePrecise() {
        Map<String, String> routes = new HashMap<>();
        routes.put("Billy Loomis", "p3");
        routes.put("*", "end_lose");
        Puzzle p = new Puzzle("p2", "qcm", "?", null, List.of("Billy Loomis"), routes);

        assertEquals("p3", p.getNextPuzzleId("Billy Loomis"));
    }

    /**
     * getNextPuzzleId doit retourner la route * pour toute réponse sans clé correspondante.
     */
    @Test
    void testRouteParDefaut() {
        Map<String, String> routes = new HashMap<>();
        routes.put("3,1415", "p2");
        routes.put("*", "end_lose");
        Puzzle p = new Puzzle("p1bis", "text", "?", null, List.of(), routes);

        assertEquals("end_lose", p.getNextPuzzleId("mauvaise_reponse"));
    }

    /**
     * Si aucune route ne correspond et qu'il n'y a pas de route *,
     * getNextPuzzleId doit retourner null.
     */
    @Test
    void testAucuneRouteRetourneNull() {
        Map<String, String> routes = new HashMap<>();
        routes.put("ok", "p2");
        // Pas de route *
        Puzzle p = new Puzzle("p1", "text", "?", null, List.of(), routes);

        assertNull(p.getNextPuzzleId("reponse_inconnue"),
            "Sans route *, getNextPuzzleId doit retourner null");
    }
}

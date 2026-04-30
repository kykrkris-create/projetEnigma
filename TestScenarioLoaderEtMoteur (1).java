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
 * <p>Tests du ScenarioLoader :
 * <ul>
 *   <li>Chargement d'un scénario valide.</li>
 *   <li>Rejet si manifest.json absent.</li>
 *   <li>Rejet si image manquante.</li>
 *   <li>Rejet si schemaVersion inconnue.</li>
 *   <li>Rejet si route vers énigme inexistante.</li>
 *   <li>Rejet si type d'énigme non supporté.</li>
 * </ul>
 *
 * <p>Tests du Moteur :
 * <ul>
 *   <li>Démarrage sans exception.</li>
 *   <li>Progression avec bonne réponse.</li>
 *   <li>Gestion de la route par défaut (*).</li>
 *   <li>Parcours complet victoire.</li>
 *   <li>Parcours complet défaite.</li>
 * </ul>
 *
 * <p>Tests de Puzzle.getNextPuzzleId :
 * <ul>
 *   <li>Route précise.</li>
 *   <li>Route par défaut (*).</li>
 *   <li>Absence de route → null.</li>
 * </ul>
 *
 * @author Lou-Ann
 */
class TestScenarioLoaderEtMoteur {

    // -----------------------------------------------------------------------
    // Utilitaire : création d'un scénario temporaire valide sur disque
    // -----------------------------------------------------------------------

    /**
     * Crée un dossier de scénario temporaire valide contenant
     * un manifest.json correct et un dossier images/ avec des images factices.
     *
     * @return le dossier racine du scénario temporaire.
     * @throws IOException en cas d'erreur d'écriture.
     */
    private File creerScenarioValide() throws IOException {
        Path dossier = Files.createTempDirectory("scenario_test");
        Path images = dossier.resolve("images");
        Files.createDirectories(images);

        // Images factices : 1 octet suffit, on teste seulement leur existence
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
     * Un scénario avec schemaVersion différente de 1 doit être rejeté.
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
     * Une route pointant vers une énigme inexistante (p99) doit
     * être rejetée lors du chargement, avec p99 mentionné dans l'erreur.
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
     * Un type d'énigme inconnu du moteur (ex : "slide") doit être rejeté,
     * avec le nom du type mentionné dans l'erreur.
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
    //  TESTS — Moteur  (mode headless pour éviter d'ouvrir une fenêtre)
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
        routesP2.put("ok", "end_win");
        routesP2.put("ko", "end_lose");

        Puzzle p1 = new Puzzle("p1", "text",    "Q1", null, List.of(), routesP1);
        Puzzle p2 = new Puzzle("p2", "boolean", "Q2", null, List.of("ok", "ko"), routesP2);

        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put("p1", p1);
        puzzles.put("p2", p2);

        return new Scenario("id_test", "Test", "JUnit", "p1", puzzles, "/tmp");
    }

    /**
     * Après demarrer(), le moteur doit afficher la première énigme
     * sans lever d'exception.
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
     * Une bonne réponse à p1 ("oui") doit faire progresser le moteur vers p2
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
     * Une réponse inconnue avec une route * définie doit être absorbée
     * sans exception (redirection vers end_lose).
     */
    @Test
    void testProgressionMauvaiseReponseAvecEtoile() {
        System.setProperty("java.awt.headless", "true");
        Scenario scenario = creerScenarioEnMemoire();
        JFrame fenetre = new JFrame();
        Moteur moteur = new Moteur(scenario, fenetre);
        moteur.demarrer();

        assertDoesNotThrow(() -> moteur.onReponse("reponse_inconnue"),
            "La route * doit absorber toute réponse non reconnue");
        fenetre.dispose();
    }

    /**
     * Parcours complet victoire : p1("oui") → p2("ok") → end_win.
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
    //  TESTS — Puzzle.getNextPuzzleId
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

        assertEquals("p3", p.getNextPuzzleId("Billy Loomis"),
            "La bonne réponse doit pointer vers p3");
    }

    /**
     * getNextPuzzleId doit retourner la destination de * quand la clé est absente.
     */
    @Test
    void testRouteParDefaut() {
        Map<String, String> routes = new HashMap<>();
        routes.put("3,1415", "p2");
        routes.put("*", "end_lose");
        Puzzle p = new Puzzle("p1bis", "text", "?", null, List.of(), routes);

        assertEquals("end_lose", p.getNextPuzzleId("mauvaise_reponse"),
            "Une réponse inconnue doit utiliser la route *");
    }

    /**
     * Sans route * et sans clé correspondante, getNextPuzzleId doit retourner null.
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

    // -----------------------------------------------------------------------
    //  TESTS — Enigme1bis (logique pure, sans Swing)
    // -----------------------------------------------------------------------

    /**
     * estBonneReponse doit accepter 3,1415 (virgule française).
     */
    @Test
    void testEnigme1bisAccepteVirgule() {
        // On passe un ecouteur vide car on ne teste que la logique
        Enigme1bis e = new Enigme1bis(r -> {});
        assertTrue(e.estBonneReponse("3,1415"),
            "3,1415 doit être accepté");
    }

    /**
     * estBonneReponse doit accepter 3.1415 (point anglais).
     */
    @Test
    void testEnigme1bisAcceptePoint() {
        Enigme1bis e = new Enigme1bis(r -> {});
        assertTrue(e.estBonneReponse("3.1415"),
            "3.1415 doit être accepté");
    }

    /**
     * estBonneReponse doit rejeter toute autre valeur.
     */
    @Test
    void testEnigme1bisRejetteMauvaiseValeur() {
        Enigme1bis e = new Enigme1bis(r -> {});
        assertFalse(e.estBonneReponse("3.14"),
            "3.14 ne doit pas être accepté");
        assertFalse(e.estBonneReponse(""),
            "Une chaîne vide ne doit pas être acceptée");
        assertFalse(e.estBonneReponse("pi"),
            "'pi' ne doit pas être accepté");
    }
}

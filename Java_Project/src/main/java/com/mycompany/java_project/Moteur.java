package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;

/** 
 * @author Rania
 */
public class Moteur {

    private Scenario scenario;
    private JFrame fenetre;
    private Puzzle puzzleCourant;
    private int nbEnigmesResolues = 0;

    public Moteur(Scenario scenario, JFrame fenetre) {
        this.scenario = scenario;
        this.fenetre = fenetre;
    }

    public void demarrer() {
        afficherPuzzle(scenario.getStartPuzzleId());
    }

    public void afficherPuzzle(String puzzleId) {
        if (puzzleId.startsWith("end_")) {
            afficherFin(puzzleId);
            return;
        }

        Puzzle p = scenario.getPuzzle(puzzleId);
        if (p == null) {
            JOptionPane.showMessageDialog(fenetre, "Énigme introuvable : " + puzzleId);
            return;
        }

        this.puzzleCourant = p;
        JPanel panel;

        if (p.getType().equals("qcm") || p.getType().equals("boolean")) {
            panel = new PanelQcm(p, scenario.getDossierScenario(), this);
        } else if (p.getType().equals("text")) {
            panel = new PanelTexte(p, scenario.getDossierScenario(), this, 3);
        } else {
            JOptionPane.showMessageDialog(fenetre, "Type non supporté : " + p.getType());
            return;
        }

        fenetre.setContentPane(panel);
        fenetre.revalidate();
        fenetre.repaint();
    }

    public void onReponse(String reponse) {
        nbEnigmesResolues++;
        String prochain = puzzleCourant.getNextPuzzleId(reponse);
        if (prochain == null) {
            JOptionPane.showMessageDialog(fenetre, "Aucune route valide pour : " + reponse);
            return;
        }
        afficherPuzzle(prochain);
    }

    private void afficherFin(String endId) {
        boolean victoire = endId.equals("end_win");

        if (victoire) {
            // Victoire : on garde la musique d'ambiance qui continue de tourner
        } else {
            // Défaite : on coupe la musique et on joue le son creepy
            SonPlayer.arreterMusique();
            String cheminSon = scenario.getDossierScenario() + "/sounds/gameover.wav";
            SonPlayer.jouer(cheminSon);
        }

        String messagePersonnalise = getMessageFin(endId);
        Fin ecran = new Fin(victoire, nbEnigmesResolues, messagePersonnalise);
        fenetre.setContentPane(ecran);
        fenetre.revalidate();
        fenetre.repaint();
    }

    /** Retourne un message personnalisé selon le type de fin. */
    private String getMessageFin(String endId) {
        switch (endId) {
            case "end_win":
                return "La maison se souvient de toi. Elle t'attendra :) ";
            case "end_lose_pi":
                return "3,1416... Tu aurais dû le savoir quand meme :) ";
            case "end_lose_scream":
                return "Le meurtrier sort de l'ombre... Trop tard pour fuir :) ";
            case "end_lose_meurtre":
                return "M.E.U.R.T.R.E. Tu l'épelleras dans ta tombe :) ";
            case "end_lose_charade":
                return "Tu n'as pas compris la charade. C'est pas grave, tu vas la vivre de toute façon :) ";
            case "end_lose_hublot":
                return "La porte ronde aime les visiteurs. Elle ne les laisse plus jamais sortir...";
            default:
                return "Tu n'as pas perdu. On a juste décidé que c'était fini pour toi...";
        }
    }
}
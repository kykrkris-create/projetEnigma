/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;

/**
 * Moteur du jeu : gère le scénario et la navigation.
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
        JPanel panelFin = new JPanel(new BorderLayout());
        panelFin.setBackground(Color.BLACK);

        JLabel labelTitre = new JLabel(victoire ? "VICTOIRE" : "FIN", JLabel.CENTER);
        labelTitre.setFont(new Font("Serif", Font.BOLD, 48));
        labelTitre.setForeground(victoire ? new Color(200, 170, 100) : new Color(180, 30, 30));

        String texte = victoire ? "Tu as résolu tous les mystères !" : "Tu n'as pas survécu...";
        JLabel labelMessage = new JLabel(texte + "  -  Énigmes résolues : " + nbEnigmesResolues, JLabel.CENTER);
        labelMessage.setForeground(Color.WHITE);
        labelMessage.setFont(new Font("Serif", Font.PLAIN, 22));

        panelFin.add(labelTitre, BorderLayout.NORTH);
        panelFin.add(labelMessage, BorderLayout.CENTER);

        fenetre.setContentPane(panelFin);
        fenetre.revalidate();
        fenetre.repaint();
    }
}
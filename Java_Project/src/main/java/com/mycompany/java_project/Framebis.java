/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Framebis
        extends JFrame {

    public Framebis() {
        this.setTitle("Escape Game - La Maison Hantée");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);

        try {
            File dossierScenario = new File("scenarios/maison_hantee");
            ScenarioLoader loader = new ScenarioLoader();
            Scenario scenario = loader.charger(dossierScenario);
            Moteur moteur = new Moteur(scenario, this);
            moteur.demarrer();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Frame::new);
    }
}
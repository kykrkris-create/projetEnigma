/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Le main
 *
 * @author Rania
 */
public class Framebis extends JFrame { 
    private Debut debutPanel;
    private String pseudo;

    public Framebis() {
        this.setTitle("Enigma");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null); 
//Pour arreter la musique des qu'on ferme la fenetre
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SonPlayer.arreterMusique();
            }
        }); 
//Pour demarer la musique d'ambiance gothique des qu'on demarre 
        File dossierScenario = new File("scenarios/maison_hantee");
        SonPlayer.jouerEnBoucle(dossierScenario.getAbsolutePath() + "/sounds/ambiance.wav"); 
        debutPanel = new Debut(this::demarrerJeu);
        this.setContentPane(debutPanel); 
        this.setVisible(true);
    } 
    private void demarrerJeu() {
        this.pseudo = debutPanel.getPseudo();
        try {
            File dossierScenario = new File("scenarios/maison_hantee");
            ScenarioLoader loader = new ScenarioLoader();
            Scenario scenario = loader.charger(dossierScenario);

            Moteur moteur = new Moteur(scenario, this);
            moteur.demarrer();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    } 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Framebis());
    }
}
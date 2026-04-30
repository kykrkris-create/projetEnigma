/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Pour ajouter la musique d'ambiance pour le jeu
 * @author Rania
 */
public class SonPlayer { 
    private static Clip musiqueAmbiance; 
    public static void jouerEnBoucle(String cheminFichier) {
        arreterMusique();
        try {
            File fichier = new File(cheminFichier);
            if (!fichier.exists()) {
                System.err.println("Son introuvable : " + cheminFichier);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(fichier);
            musiqueAmbiance = AudioSystem.getClip();
            musiqueAmbiance.open(audio);
            musiqueAmbiance.loop(Clip.LOOP_CONTINUOUSLY);
            musiqueAmbiance.start();
        } catch (Exception e) {
            System.err.println("Erreur lecture musique : " + e.getMessage());
        }
    }
//Pour arreter tout son en cours
    public static void arreterMusique() {
        if (musiqueAmbiance != null && musiqueAmbiance.isRunning()) {
            musiqueAmbiance.stop();
            musiqueAmbiance.close();
        }
    }
    
    public static void jouer(String cheminFichier) {
        try {
            File fichier = new File(cheminFichier);
            if (!fichier.exists()) {
                System.err.println("Son introuvable : " + cheminFichier);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(fichier);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            
            clip.start();
        } catch (Exception e) {
            System.err.println("Erreur lecture son : " + e.getMessage());
        }
    }
}
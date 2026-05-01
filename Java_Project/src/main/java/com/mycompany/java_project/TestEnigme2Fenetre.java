package com.mycompany.java_project;

import javax.swing.*;

/**
 * Fenêtre de test standalone pour Enigme2.
 * Permet de tester l'affichage et la logique de l'énigme
 * sans lancer tout le jeu.
 *
 * @author Lou-Ann
 */
public class TestEnigme2Fenetre {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Enigme 2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            // EcouteurReponse anonyme : affiche juste la réponse transmise
            EcouteurReponse ecouteurTest = reponse ->
                JOptionPane.showMessageDialog(frame,
                    "Réponse transmise au moteur : " + reponse);

            frame.setContentPane(new Enigme2(ecouteurTest));
            frame.setVisible(true);
        });
    }
}

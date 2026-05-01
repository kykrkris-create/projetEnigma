/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * @author Rania
 */
public class PanelQcm extends JPanel implements ActionListener { 
    private Puzzle puzzle;
    private Moteur moteur;
    private JButton[] boutons;
    private Image imageFond;

    public PanelQcm(Puzzle puzzle, String dossierScenario, Moteur moteur) {
        this.puzzle = puzzle;
        this.moteur = moteur; 
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); 
        File imgFile = new File(dossierScenario, puzzle.getImage());
        if (imgFile.exists()) {
            imageFond = new ImageIcon(imgFile.getAbsolutePath()).getImage();
        } 
        Font police = new Font("Serif", Font.BOLD, 16);
        // on melange les choix a chaque affichage
        List<String> choixMelanges = new ArrayList<>(puzzle.getChoices());
        Collections.shuffle(choixMelanges);
        boutons = new JButton[choixMelanges.size()];
        for (int i = 0; i < choixMelanges.size(); i++) {
            JButton b = new JButton(choixMelanges.get(i));
            styliserBouton(b);
            b.setFont(police);
            b.addActionListener(this);
            boutons[i] = b;
        } 
        JPanel panelImage = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageFond != null) {
                    g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
                }
            } 
            @Override
            public void doLayout() {
                int w = getWidth();
                int h = getHeight();
                int btnW = 160;
                int btnH = 40;
                int y = h - btnH - 20;
                int n = boutons.length;
                for (int i = 0; i < n; i++) {
                    int x = (int)(w * (i + 1.0) / (n + 1.0)) - btnW / 2;
                    boutons[i].setBounds(x, y, btnW, btnH);
                }
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        for (JButton b : boutons) panelImage.add(b);
        this.add(panelImage, BorderLayout.CENTER); 
        JLabel labelConsigne = new JLabel(puzzle.getPrompt(), JLabel.CENTER);
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 18));
        labelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        labelConsigne.setOpaque(true);
        labelConsigne.setBackground(Color.BLACK);
        this.add(labelConsigne, BorderLayout.SOUTH);
    } 
    private void styliserBouton(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true);
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        for (JButton b : boutons) {
            if (e.getSource() == b) {
                moteur.onReponse(b.getText());
                return;
            }
        }
    }
}
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

/**
 * Panel pour énigmes de type texte avec tentatives limitées.
 * @author Rania
 */
public class PanelTexte extends JPanel implements ActionListener {

    private Puzzle puzzle;
    private Moteur moteur;
    private int tentativesRestantes;

    private JTextField champReponse;
    private JButton boutonValider;
    private JLabel labelTentatives;
    private Image imageFond;

    public PanelTexte(Puzzle puzzle, String dossierScenario, Moteur moteur, int tentativesMax) {
        this.puzzle = puzzle;
        this.moteur = moteur;
        this.tentativesRestantes = tentativesMax;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        File imgFile = new File(dossierScenario, puzzle.getImage());
        if (imgFile.exists()) {
            imageFond = new ImageIcon(imgFile.getAbsolutePath()).getImage();
        }

        Font police = new Font("Serif", Font.BOLD, 16);

        champReponse = new JTextField();
        champReponse.setFont(police);
        champReponse.setBackground(Color.BLACK);
        champReponse.setForeground(Color.WHITE);
        champReponse.setCaretColor(Color.WHITE);
        champReponse.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        champReponse.addActionListener(this);

        boutonValider = new JButton("Valider");
        boutonValider.setFont(police);
        boutonValider.setBackground(Color.BLACK);
        boutonValider.setForeground(Color.WHITE);
        boutonValider.setFocusPainted(false);
        boutonValider.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        boutonValider.setOpaque(true);
        boutonValider.addActionListener(this);

        labelTentatives = new JLabel("Tentatives restantes : " + tentativesRestantes, JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(new Font("Serif", Font.BOLD, 16));

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
                int champW = 250, champH = 35;
                int btnW = 120, btnH = 35;
                int espacement = 10;
                int totalW = champW + espacement + btnW;
                int xDebut = (w - totalW) / 2;
                int y = h - champH - 20;
                champReponse.setBounds(xDebut, y, champW, champH);
                boutonValider.setBounds(xDebut + champW + espacement, y, btnW, btnH);
                labelTentatives.setBounds(0, y - 30, w, 25);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        panelImage.add(champReponse);
        panelImage.add(boutonValider);
        panelImage.add(labelTentatives);
        this.add(panelImage, BorderLayout.CENTER);

        JLabel labelConsigne = new JLabel(puzzle.getPrompt(), JLabel.CENTER);
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 16));
        labelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        labelConsigne.setOpaque(true);
        labelConsigne.setBackground(Color.BLACK);
        this.add(labelConsigne, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String reponse = champReponse.getText().trim();

        if (puzzle.getRoutes().containsKey(reponse)) {
            moteur.onReponse(reponse);
            return;
        }

        tentativesRestantes--;
        if (tentativesRestantes > 0) {
            labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
            JOptionPane.showMessageDialog(this, "Mauvaise réponse...");
            champReponse.setText("");
        } else {
            moteur.onReponse(reponse);
        }
    }
}
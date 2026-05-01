/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
 * @author Rania
 */
public class Fin extends JPanel implements ActionListener {
    private boolean victoire;
    private int nbEnigmesResolues;
    private JButton boutonRejouer;
    private JButton boutonQuitter; 
    public Fin(boolean victoire, int nbEnigmesResolues) {
        this(victoire, nbEnigmesResolues,
            victoire ? "Tu as survécu à la maison hantée." : "Tu n'as pas survécu...", "");
    } 
    public Fin(boolean victoire, int nbEnigmesResolues, String message) {
        this(victoire, nbEnigmesResolues, message, "");
        
    } 
    public Fin(boolean victoire, int nbEnigmesResolues, String message, String pseudo) {
        this.victoire = victoire;
        this.nbEnigmesResolues = nbEnigmesResolues;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); 
        JLabel labelTitre = new JLabel(victoire ? "VICTOIRE" : "GAME OVER", JLabel.CENTER);
        
        labelTitre.setFont(new Font("Serif", Font.BOLD, 64));
        labelTitre.setForeground(victoire ? new Color(200, 170, 100) : new Color(180, 30, 30));
        
        labelTitre.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));
        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.PAGE_AXIS));
        panelCentre.setBackground(Color.BLACK);
        JLabel labelMessage = new JLabel(message, JLabel.CENTER);
        labelMessage.setForeground(Color.WHITE);
        
        
        labelMessage.setFont(new Font("Serif", Font.PLAIN, 22));
        labelMessage.setAlignmentX(Component.CENTER_ALIGNMENT); 
        String texteScore = pseudo.isEmpty() ? "Énigmes résolues : " + nbEnigmesResolues : pseudo + " a résolu " + nbEnigmesResolues + " énigme(s)";
        JLabel labelScore = new JLabel(texteScore, JLabel.CENTER);
        labelScore.setForeground(Color.LIGHT_GRAY);
        labelScore.setFont(new Font("Serif", Font.PLAIN, 20));
        labelScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelScore.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        panelCentre.add(Box.createVerticalGlue());
        panelCentre.add(labelMessage);
        panelCentre.add(labelScore);
        panelCentre.add(Box.createVerticalGlue());
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        panelBoutons.setBackground(Color.BLACK);
        Font policeBouton = new Font("Serif", Font.BOLD, 18);
        boutonRejouer = new JButton("Rejouer");
        
        styliserBouton(boutonRejouer);
        boutonRejouer.setFont(policeBouton);
        boutonRejouer.addActionListener(this);
        boutonQuitter = new JButton("Quitter");
        styliserBouton(boutonQuitter);
        
        boutonQuitter.setFont(policeBouton);
        boutonQuitter.addActionListener(this); 
        panelBoutons.add(boutonRejouer);
        panelBoutons.add(boutonQuitter);
        this.add(labelTitre, BorderLayout.NORTH);
        this.add(panelCentre, BorderLayout.CENTER);
        this.add(panelBoutons, BorderLayout.SOUTH);
    } private void styliserBouton(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(150, 45));
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonRejouer) {
            SonPlayer.arreterMusique();
            
            Window fenetre = SwingUtilities.getWindowAncestor(this);
            if (fenetre != null) {
                fenetre.dispose();
            }
            SwingUtilities.invokeLater(() -> new Framebis());
            
        }
        else if (e.getSource() == boutonQuitter) {
            SonPlayer.arreterMusique();
            System.exit(0);
        }
  }
}
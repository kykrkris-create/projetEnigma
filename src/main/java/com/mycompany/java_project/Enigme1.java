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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Rania
 */
public class Enigme1 extends JPanel implements ActionListener {

    private JButton button1;
    private JButton button2;
    private Image imageFond;

    public Enigme1() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        imageFond = new ImageIcon(getClass().getResource("Enigme1.png")).getImage();

        var policeBouton = new Font("Serif", Font.BOLD, 16);

        button1 = new JButton("Clé rouillée");
        styliserBouton(button1);
        button1.setFont(policeBouton);
        button1.addActionListener(this);

        button2 = new JButton("Clé moisie");
        styliserBouton(button2);
        button2.setFont(policeBouton);
        button2.addActionListener(this);

        var panelImage = new JPanel(null) {
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
                // Position centrée sous chaque clé (37% et 63% de la largeur)
                button1.setBounds((int)(w * 0.37) - btnW / 2, y, btnW, btnH);
                button2.setBounds((int)(w * 0.63) - btnW / 2, y, btnW, btnH);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        panelImage.add(button1);
        panelImage.add(button2);

        this.add(panelImage, BorderLayout.CENTER);

        var labelConsigne = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Vous trouvez deux clés posées sur la table.<br>"
            + "Laquelle choisissez-vous ?"
            + "</div></html>"
        );
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 20));
        labelConsigne.setHorizontalAlignment(JLabel.CENTER);
        labelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        labelConsigne.setOpaque(true);
        labelConsigne.setBackground(Color.BLACK);
        this.add(labelConsigne, BorderLayout.SOUTH);
    }

    /** Style gothique : fond noir, texte blanc, bordure claire. */
    private void styliserBouton(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true); // important pour que le fond noir s'affiche bien
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Navigation gérée plus tard via le manifest.json
    }
}
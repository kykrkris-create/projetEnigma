 
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
//meme methode que j'ai utilisee auparavant pour charger l'image(dans Debut.java)
        imageFond = new ImageIcon(getClass().getResource("Enigme1.png")).getImage();
        Font policeBouton = new Font("Serif", Font.BOLD, 16);
        button1 = new JButton("Clé rouillée");
        styliserBouton(button1);
        
        button1.setFont(policeBouton);
        button1.addActionListener(this);

        button2 = new JButton("Clé moisie");
        styliserBouton(button2);
        button2.setFont(policeBouton);
        button2.addActionListener(this);
//pareil;
        JPanel panelImage = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageFond != null) {
                    g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
                }
            }
//methode que j'ai bien aimee , qui permet de redimensionner la fenetre de facon
//que les boutons les textes , et l'image dans leur place qlq soit la taille de la fenetre 
            @Override
            public void doLayout() {
                int w = getWidth();
                int h = getHeight();
                int btnW = 160;
                int btnH = 40;
                int y = h - btnH - 20;
                button1.setBounds((int)(w * 0.37) - btnW / 2, y, btnW, btnH);
                button2.setBounds((int)(w * 0.63) - btnW / 2, y, btnW, btnH);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        panelImage.add(button1); 
        this.add(panelImage, BorderLayout.CENTER); 
        JPanel panelConsigne = new JPanel();
        panelConsigne.setLayout(new BoxLayout(panelConsigne, BoxLayout.PAGE_AXIS));
        panelConsigne.setBackground(Color.BLACK);
        panelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10)); 
        JLabel labelConsigne1 = new JLabel("Vous trouvez deux clés posées sur la table.", JLabel.CENTER);
        labelConsigne1.setForeground(Color.WHITE);
        labelConsigne1.setFont(new Font("Serif", Font.PLAIN, 20));
        labelConsigne1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel labelConsigne2 = new JLabel("Laquelle choisissez-vous ?", JLabel.CENTER);
        labelConsigne2.setForeground(Color.WHITE);
        labelConsigne2.setFont(new Font("Serif", Font.PLAIN, 20));
        labelConsigne2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConsigne.add(labelConsigne1);
        panelConsigne.add(labelConsigne2);
        this.add(panelConsigne, BorderLayout.SOUTH);
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
        if (e.getSource() == button1) {
            JOptionPane.showMessageDialog(this,"Vous avez choisi la clé rouillée");
        } else if (e.getSource() == button2) {
            JOptionPane.showMessageDialog(this,"Vous avez choisi la clé moisie");
        }
    }
}
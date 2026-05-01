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
public class Enigme5 extends JPanel implements ActionListener {
    private JButton boutonCarre;
    private JButton boutonTriangle;
    private JButton boutonRond;
    private Image imageFond;
    public Enigme5() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
        imageFond = new ImageIcon(getClass().getResource("Enigme5.png")).getImage();
        Font policeBouton = new Font("Serif", Font.BOLD, 16);
        boutonCarre = new JButton("Porte carré");
        styliserBouton(boutonCarre);
        boutonCarre.setFont(policeBouton);
        boutonCarre.addActionListener(this);
        boutonTriangle = new JButton("Porte triangle");
        styliserBouton(boutonTriangle);
        boutonTriangle.setFont(policeBouton);
        
        boutonTriangle.addActionListener(this); 
        boutonRond = new JButton("Porte ronde");
        styliserBouton(boutonRond);
        boutonRond.setFont(policeBouton);
        boutonRond.addActionListener(this); 
        JPanel panelImage = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                if (imageFond != null) {
                    g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
                }
            }
//
            @Override
            public void doLayout() {
                int w = getWidth();
                
                
                int h = getHeight();
                int btnW = 140;
                int btnH = 40;
                int y = h - btnH - 20;
                boutonCarre.setBounds((int)(w * 0.22) - btnW / 2, y, btnW, btnH);
                boutonTriangle.setBounds((int)(w * 0.50) - btnW / 2, y, btnW, btnH);
                boutonRond.setBounds((int)(w * 0.78) - btnW / 2, y, btnW, btnH);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        
        
        panelImage.add(boutonCarre);
        panelImage.add(boutonTriangle);
        panelImage.add(boutonRond); 
        this.add(panelImage, BorderLayout.CENTER); 
        JLabel labelConsigne = new JLabel("Choisis la porte qui scellera ton destin...", JLabel.CENTER);
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 20));
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
        if (e.getSource() == boutonCarre) {
            JOptionPane.showMessageDialog(this, "Vous avez choisi la porte carré");
        } else if (e.getSource() == boutonTriangle) {
            
            JOptionPane.showMessageDialog(this, "Vous avez choisi la porte triangle");
        } else if (e.getSource() == boutonRond) {
            JOptionPane.showMessageDialog(this, "Vous avez choisi la porte ronde");
        }
    }
}
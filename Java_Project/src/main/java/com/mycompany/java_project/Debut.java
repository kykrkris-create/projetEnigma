 
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
 * @author X515, Rania
 */
public class Debut extends JPanel implements ActionListener { 
    private JTextField textPseudo;
    private JButton startBouton;
    private Image imageFond;
    private Runnable onStart;

    private final String texteHistoire = "<html><body style='text-align:center;'>"
            + "La nuit est déjà tombée lorsque vous atteignez la vieille demeure, "
            + "perdue au milieu d'un terrain noyé dans le brouillard. "
            + "Le silence est presque total… sauf le vent qui fait grincer le portail rouillé, "
            + "comme s'il protestait contre votre arrivée. "
            + "Un frisson vous parcourt. Cette impression oppressante… celle d'être observé."
            + "<br><br>"
            + "Si vous voulez découvrir ce qui se cache derrière ces murs, "
            + "il va falloir trouver le courage d'en franchir le seuil."
            + "</body></html>";
    public Debut() {
    this(null); 
}   
 public Debut(Runnable onStart) {
        this.onStart = onStart;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
//on verifie que l'image existe avant sinon ca plante:
        java.net.URL urlImage = getClass().getResource("maison.png");
        if (urlImage != null) {
        imageFond = new ImageIcon(urlImage).getImage();
} 
        Font policeBouton = new Font("Serif", Font.BOLD, 16); 
        textPseudo = new JTextField(20);
        textPseudo.setFont(new Font("Serif", Font.PLAIN, 16));
        textPseudo.setBackground(Color.BLACK);
        textPseudo.setForeground(Color.WHITE);
        textPseudo.setCaretColor(Color.WHITE);
        textPseudo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        startBouton = new JButton("START");
        styliserBouton(startBouton);
        startBouton.setFont(policeBouton);
        startBouton.addActionListener(this);
//Panel ou ca contient les images
        JPanel panelImage = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageFond != null) {
                    g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        this.add(panelImage, BorderLayout.CENTER); 
        JPanel panelBas = new JPanel();
        panelBas.setLayout(new BoxLayout(panelBas, BoxLayout.PAGE_AXIS));
        panelBas.setBackground(Color.BLACK);
        panelBas.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); 
        JLabel labelHistoire = new JLabel(texteHistoire);
        labelHistoire.setForeground(Color.LIGHT_GRAY);
        labelHistoire.setFont(new Font("Serif", Font.PLAIN, 16));
        labelHistoire.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel pseudoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pseudoPanel.setBackground(Color.BLACK);

        JLabel labelPseudo = new JLabel("Pseudo : ");
        labelPseudo.setFont(new Font("Serif", Font.PLAIN, 16));
        labelPseudo.setForeground(Color.WHITE);
        pseudoPanel.add(labelPseudo);
        pseudoPanel.add(textPseudo);
        pseudoPanel.add(startBouton);
        panelBas.add(labelHistoire);
        panelBas.add(Box.createVerticalStrut(10));
        panelBas.add(pseudoPanel);
        this.add(panelBas, BorderLayout.SOUTH);
    }
//Pour styliser les boutons avec un theme goth
    private void styliserBouton(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true);
    } 
    public String getPseudo() {
        return textPseudo.getText().trim();
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startBouton) {
            String pseudo = getPseudo();
            if (pseudo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Saisis un pseudo pour commencer :) ");
                return;
            }
            if (onStart != null) {
            onStart.run();
        }
    }
    }
}
    
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
public class Enigme4 extends JPanel implements ActionListener {
    private JTextField champReponse;
    private JButton boutonValider;
    private JLabel labelTentatives;
    private int tentativesRestantes = 3;
    
    private static final String BONNE_REPONSE = "ENTERREMENT";
    private Image imageFond;

    public Enigme4() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); 
        imageFond = new ImageIcon(getClass().getResource("Enigme4.png")).getImage(); 
//dessine l'image en fond et contient le champ et les boutons centrés en bas
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
                int cW = 250;
                int cH = 35;
                int btnW = 120;
                int btnH = 35;
                int e= 10;
                int t = cW + e+ btnW;
                int xDebut=(w-t)/2;
                int y =h -cH -0; 
                champReponse.setBounds(xDebut, y, cW, cH);
                boutonValider.setBounds(xDebut + cW +e, y, btnW, btnH);
                labelTentatives.setBounds(0, y - 30, w, 25);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480)); 
        var policeChamp = new Font("Serif", Font.BOLD, 16); 
        champReponse = new JTextField();
        champReponse.setFont(policeChamp);
        champReponse.setBackground(Color.BLACK);
        champReponse.setForeground(Color.WHITE);
        champReponse.setCaretColor(Color.WHITE);
        champReponse.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        champReponse.addActionListener(this);  
        boutonValider = new JButton("Valider");
        boutonValider.setFont(policeChamp);
        boutonValider.setBackground(Color.BLACK);
        boutonValider.setForeground(Color.WHITE);
        boutonValider.setFocusPainted(false);
        
        
        boutonValider.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        boutonValider.setOpaque(true);
        boutonValider.addActionListener(this); 
        labelTentatives = new JLabel("Tentatives restantes : 3", JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(new Font("Serif", Font.BOLD, 16)); 
        panelImage.add(champReponse);
        panelImage.add(boutonValider);
        panelImage.add(labelTentatives); 
        this.add(panelImage, BorderLayout.CENTER); 
        JLabel labelConsigne = new JLabel("Résolvez la charade pour continuer.", JLabel.CENTER);
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 20));
        labelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        labelConsigne.setOpaque(true);
        
        
        labelConsigne.setBackground(Color.BLACK);
        this.add(labelConsigne, BorderLayout.SOUTH);
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        String reponse = champReponse.getText().trim().toUpperCase();

        if (reponse.equals(BONNE_REPONSE)) {
            JOptionPane.showMessageDialog(this,
                "Bonne réponse ! Vous pouvez continuer.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);//Existe deja dans swing 
        } else {
            
            tentativesRestantes--;
            if (tentativesRestantes > 0) {
                labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
                
                JOptionPane.showMessageDialog(this,
                    "Mauvaise réponse...",
                    "Erreur", JOptionPane.WARNING_MESSAGE);//pareil
                champReponse.setText("");
            } else {
                
                labelTentatives.setText("Plus de tentatives.");
                champReponse.setEnabled(false);
                boutonValider.setEnabled(false);
                
                JOptionPane.showMessageDialog(this,
                    "Vous avez épuisé vos tentatives. La réponse était : " + BONNE_REPONSE,
                    "Échec", JOptionPane.ERROR_MESSAGE);// 
            }
             }
    }
    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author X515
 */
public class Debut extends JPanel{
    private String a = "<html><body>"
            +"La nuit est déjà tombée lorsque vous atteignez la vieille demeure, perdue au milieu d’un terrain noyé dans le brouillard. "
            +"Le silence est presque total… sauf le vent qui fait grincer le portail rouillé, comme s’il protestait contre votre arrivée. " 
            +"Un frisson vous parcourt. Cette impression oppressante… celle d’être observé.<br><br>"
            +"Si vous voulez découvrir ce qui se cache derrière ces murs, il va falloir trouver le courage d’en franchir le seuil.<br><br>" 
            +"</body></html>";
    private String b = "Crée un pseudo et commencer la partie. ";
    
    
    public Debut(){
        
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
        
        JLabel texte1 = new JLabel(a);
        texte1.setFont(new Font("Serif", Font.PLAIN, 25));
        texte1.setForeground(Color.LIGHT_GRAY);

        ImageIcon icon = new ImageIcon("C:\\Users\\YOGA 6\\Downloads\\projetEnigma-master\\projetEnigma-master\\Java_Project\\src\\main\\java\\com\\mycompany\\java_project\\maison.png");
        JLabel  image_maison = new JLabel(icon);
        
        JPanel pseudoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pseudoPanel.setBackground(Color.LIGHT_GRAY);
        
        JLabel texte2 = new JLabel(b);
        texte2.setFont(new Font("Serif", Font.PLAIN, 20));
        
        JLabel labelPseudo = new JLabel("Pseudo : ");
        labelPseudo.setFont(new Font("Serif", Font.PLAIN, 20));
        
        
        JTextField textPseudo = new JTextField(30);
        textPseudo.setFont(new Font("Serif", Font.PLAIN, 20));
        
        JButton startBouton = new JButton("START");
    
         pseudoPanel.add(texte2);
         pseudoPanel.add(labelPseudo);
         pseudoPanel.add(textPseudo);
         pseudoPanel.add(startBouton);
         
        
        
        this.add(image_maison, BorderLayout.NORTH);
        this.add(texte1, BorderLayout.CENTER);
        this.add(pseudoPanel, BorderLayout.SOUTH);
        
    
    }
    
}

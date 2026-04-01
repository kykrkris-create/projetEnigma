/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import java.awt.Color;
import java.awt.HeadlessException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/** 
*
 * @author X515
 */
public class Frame extends JFrame {
    
   private Debut debut;
    
    
    
    public Frame() throws HeadlessException {
        // Fermeture de la fenêtre => fermeture du programme
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Construction du contenu
        setup();
        this.pack();
        // Centrage de la fenêtre sur l'écran
        this.setLocationRelativeTo(null);
                   
        this.setVisible(true);
    }
    
     private void setup() {
        Box box = new Box(BoxLayout.PAGE_AXIS);     
        this.setContentPane(box); 
        box.setBackground(Color.BLACK);
        box.setOpaque(true);
        this.debut = new Debut();
        this.add(debut);
     }
     
    }


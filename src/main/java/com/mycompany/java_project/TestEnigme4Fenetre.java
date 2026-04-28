/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.JFrame;

/**
 *
 * @author Rania
 */
public class TestEnigme4Fenetre {
        public static void main(String[] args) {
        JFrame frame = new JFrame("Test Enigme 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setContentPane(new Enigme4());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}

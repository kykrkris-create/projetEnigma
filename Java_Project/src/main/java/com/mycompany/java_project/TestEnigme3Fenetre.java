/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.*;

/** 
 * @author Rania
 */
public class TestEnigme3Fenetre {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Enigme 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        
        frame.setContentPane(new Enigme3());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    
}
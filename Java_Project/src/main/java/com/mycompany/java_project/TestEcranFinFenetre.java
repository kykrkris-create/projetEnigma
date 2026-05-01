/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import javax.swing.*;

/** 
 * @author Rania
 */
public class TestEcranFinFenetre {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test EcranFin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        // Change true en false pour tester la défaite
        frame.setContentPane(new Fin(true, 5));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
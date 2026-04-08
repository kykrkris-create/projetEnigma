/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

/**
 *
 * @author YOGA 6
 */
import javax.swing.*;
import java.awt.*;

public class Enigme1  extends JPanel{

    public Enigme1 () {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        var icon = new ImageIcon("C:\\Users\\YOGA 6\\Downloads\\projetEnigma-master\\projetEnigma-master\\Java_Project\\src\\main\\java\\com\\mycompany\\java_project\\Enigme1.png");

        var imageLabel = new JLabel(icon);
        this.add(imageLabel, BorderLayout.CENTER);

        var panelDuBas = new JPanel();
        panelDuBas.setLayout(new BoxLayout(panelDuBas, BoxLayout.PAGE_AXIS));
        panelDuBas.setBackground(Color.BLACK);
        var label1 = new JLabel("Vous trouvez deux clés posées sur la table");
        var label2 = new JLabel("Laquelle choisissez-vous ?");
        panelDuBas.add(label1);
        panelDuBas.add(label2);

        var panelDesBoutons = new JPanel(new FlowLayout());
        panelDesBoutons.setBackground(Color.BLACK);
        var button1 = new JButton("Clé rouillée");
        var button2 = new JButton("Clé moisie");
        panelDesBoutons.add(button1);
        panelDesBoutons.add(button2);
        panelDuBas.add(panelDesBoutons);

        this.add(panelDuBas, BorderLayout.SOUTH);
    }
}

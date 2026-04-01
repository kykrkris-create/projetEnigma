/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_project;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author X515
 */
public class Debut extends JPanel{
    private String a = "  La nuit tombe lorsque vous arrivez devant la vieille demeure, isolée au milieu d’un terrain envahi par le brouillard.  LALALALLALALALALALALALALLA";
    private String b = "  Un silence étrange règne, seulement brisé par le vent qui fait grincer le portail rouillé.  ";
    private String c = "  Une sensation de malaise vous serre la poitrine, comme si quelqu’un vous observait déjà.  ";
    private String d = "  Pour comprendre ce qui se cache ici, il faudra trouver le courage de franchir le seuil.  ";
    
    
    public Debut(){
        // Panel principal avec un layout box
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
        
        JLabel ligne1 = new JLabel(a);
        JLabel ligne2 = new JLabel(b);
        JLabel ligne3 = new JLabel(c);
        JLabel ligne4 = new JLabel(d);
        ligne1.setFont(new Font("Serif", Font.TRUETYPE_FONT, 18));
        ligne1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ligne2.setFont(new Font("Serif", Font.TRUETYPE_FONT, 18));
        ligne2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ligne3.setFont(new Font("Serif", Font.TRUETYPE_FONT, 18));
        ligne3.setAlignmentX(Component.CENTER_ALIGNMENT);
        ligne4.setFont(new Font("Serif", Font.TRUETYPE_FONT, 25));
        ligne4.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        ImageIcon icon = new ImageIcon("C:\\Users\\X515\\Desktop\\Projet_Java\\Java_Project\\src\\main\\java\\com\\mycompany\\java_project\\maison.png");
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        this.add(ligne1);
        this.add(ligne2);
        this.add(ligne3);
        this.add(ligne4);
        
        this.add(new JLabel(icon));
    
    }
    
}

package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Enigme 2 : QCM sur le meurtrier de Scream 1.
 * Bonne réponse : Billy Loomis → Enigme 3.
 * 2 tentatives max. Echec → EcranFin (perdu).
 *
 * @author Lou-Ann
 */
public class Enigme2 extends JPanel implements ActionListener {

    private Frame frame;

    private JButton boutonRoman;
    private JButton boutonBilly;
    private JButton boutonMickey;
    private JLabel labelTentatives;
    private int tentativesRestantes = 2;

    private Image imageFond;

    public Enigme2(Frame frame) {
        this.frame = frame;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        imageFond = new ImageIcon(getClass().getResource("enigme2.png")).getImage();

        var police = new Font("Serif", Font.BOLD, 16);

        boutonRoman  = new JButton("Roman Bridger");
        boutonBilly  = new JButton("Billy Loomis");
        boutonMickey = new JButton("Mickey Altieri");

        styliserBouton(boutonRoman, police);
        styliserBouton(boutonBilly, police);
        styliserBouton(boutonMickey, police);

        boutonRoman.addActionListener(this);
        boutonBilly.addActionListener(this);
        boutonMickey.addActionListener(this);

        labelTentatives = new JLabel("Tentatives restantes : 2", JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(police);

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
                int w = getWidth(), h = getHeight();
                int btnW = 180, btnH = 40, esp = 20;
                int xDebut = (w - 3 * btnW - 2 * esp) / 2;
                int y = h - btnH - 20;
                boutonRoman.setBounds(xDebut, y, btnW, btnH);
                boutonBilly.setBounds(xDebut + btnW + esp, y, btnW, btnH);
                boutonMickey.setBounds(xDebut + 2 * (btnW + esp), y, btnW, btnH);
                labelTentatives.setBounds(0, y - 30, w, 25);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        panelImage.add(boutonRoman);
        panelImage.add(boutonBilly);
        panelImage.add(boutonMickey);
        panelImage.add(labelTentatives);
        this.add(panelImage, BorderLayout.CENTER);

        var labelConsigne = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Une alarme retentit dans la maison...<br>"
            + "Comment s'appelle le meurtrier dans Scream 1 ?"
            + "</div></html>"
        );
        labelConsigne.setForeground(Color.WHITE);
        labelConsigne.setFont(new Font("Serif", Font.PLAIN, 20));
        labelConsigne.setHorizontalAlignment(JLabel.CENTER);
        labelConsigne.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        labelConsigne.setOpaque(true);
        labelConsigne.setBackground(Color.BLACK);
        this.add(labelConsigne, BorderLayout.SOUTH);
    }

    private void styliserBouton(JButton b, Font police) {
        b.setFont(police);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonBilly) {
            JOptionPane.showMessageDialog(this, "Bonne réponse !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            frame.allerA(new Enigme3(frame));
        } else {
            tentativesRestantes--;
            if (tentativesRestantes > 0) {
                labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
                JOptionPane.showMessageDialog(this, "Mauvaise réponse... Réessayez !", "Erreur", JOptionPane.WARNING_MESSAGE);
            } else {
                labelTentatives.setText("Plus de tentatives.");
                boutonRoman.setEnabled(false);
                boutonBilly.setEnabled(false);
                boutonMickey.setEnabled(false);
                JOptionPane.showMessageDialog(this, "La réponse était : Billy Loomis", "Échec", JOptionPane.ERROR_MESSAGE);
                frame.allerA(new EcranFin(frame, false));
            }
        }
    }
}


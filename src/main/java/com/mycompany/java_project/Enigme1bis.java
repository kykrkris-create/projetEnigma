package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Enigme 1bis : trouver la valeur de pi avec 4 chiffres après la virgule.
 * 3 tentatives. Bonne réponse → Enigme 2. Echec → EcranFin (perdu).
 *
 * @author Lou-Ann
 */
public class Enigme1bis extends JPanel implements ActionListener {

    private Frame frame;

    private JTextField champReponse;
    private JButton boutonValider;
    private JLabel labelTentatives;
    private int tentativesRestantes = 3;

    private static final String BONNE_REPONSE_VIRGULE = "3,1415";
    private static final String BONNE_REPONSE_POINT   = "3.1415";

    private Image imageFond;

    public Enigme1bis(Frame frame) {
        this.frame = frame;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        imageFond = new ImageIcon(getClass().getResource("enigme1bis.png")).getImage();

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
                int champW = 250, champH = 35, btnW = 120, btnH = 35, esp = 10;
                int xDebut = (w - champW - esp - btnW) / 2;
                int y = h - champH - 20;
                champReponse.setBounds(xDebut, y, champW, champH);
                boutonValider.setBounds(xDebut + champW + esp, y, btnW, btnH);
                labelTentatives.setBounds(0, y - 30, w, 25);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));

        var police = new Font("Serif", Font.BOLD, 16);

        champReponse = new JTextField();
        champReponse.setFont(police);
        champReponse.setBackground(Color.BLACK);
        champReponse.setForeground(Color.WHITE);
        champReponse.setCaretColor(Color.WHITE);
        champReponse.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        champReponse.addActionListener(this);

        boutonValider = new JButton("Valider");
        boutonValider.setFont(police);
        boutonValider.setBackground(Color.BLACK);
        boutonValider.setForeground(Color.WHITE);
        boutonValider.setFocusPainted(false);
        boutonValider.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        boutonValider.setOpaque(true);
        boutonValider.addActionListener(this);

        labelTentatives = new JLabel("Tentatives restantes : 3", JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(police);

        panelImage.add(champReponse);
        panelImage.add(boutonValider);
        panelImage.add(labelTentatives);
        this.add(panelImage, BorderLayout.CENTER);

        var labelConsigne = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Vous êtes bloqué devant une pierre tombale...<br>"
            + "Quelle est la valeur de pi avec 4 chiffres après la virgule ?"
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

    Enigme1bis() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String reponse = champReponse.getText().trim();

        if (reponse.equals(BONNE_REPONSE_VIRGULE) || reponse.equals(BONNE_REPONSE_POINT)) {
            JOptionPane.showMessageDialog(this, "Bonne réponse !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            frame.allerA(new Enigme2(frame));
        } else {
            tentativesRestantes--;
            if (tentativesRestantes > 0) {
                labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
                JOptionPane.showMessageDialog(this, "Mauvaise réponse... Réessayez !", "Erreur", JOptionPane.WARNING_MESSAGE);
                champReponse.setText("");
            } else {
                labelTentatives.setText("Plus de tentatives.");
                champReponse.setEnabled(false);
                boutonValider.setEnabled(false);
                JOptionPane.showMessageDialog(this, "La réponse était : 3,1415", "Échec", JOptionPane.ERROR_MESSAGE);
                frame.allerA(new EcranFin(frame, false));
            }
        }
    }
}

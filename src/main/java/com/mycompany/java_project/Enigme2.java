package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Enigme 2 : QCM — identifier le meurtrier dans le film Scream (1996).
 *
 * <p>Règles :
 * <ul>
 *   <li>3 choix possibles : Roman Bridger, Billy Loomis (correct), Mickey Altieri.</li>
 *   <li>2 tentatives maximum.</li>
 *   <li>Bonne réponse → appel de ecouteur.onReponse("Billy Loomis") → énigme 3.</li>
 *   <li>Plus de tentatives → appel de ecouteur.onReponse avec la mauvaise réponse → end_lose.</li>
 * </ul>
 *
 * <p>Cette classe utilise l'interface {@link EcouteurReponse} pour déléguer
 * la navigation au {@link Moteur}, sans en dépendre directement.
 *
 * @author Lou-Ann
 */
public class Enigme2 extends JPanel implements ActionListener {

    /** Écouteur de réponse (implémenté par le Moteur) pour la navigation. */
    private final EcouteurReponse ecouteur;

    private JButton boutonRoman;
    private JButton boutonBilly;
    private JButton boutonMickey;
    private JLabel labelTentatives;

    /** Nombre de tentatives restantes, initialisé à 2. */
    private int tentativesRestantes = 2;

    /** Texte exact de la bonne réponse, doit correspondre à une clé dans le manifest. */
    static final String BONNE_REPONSE = "Billy Loomis";

    private Image imageFond;

    /**
     * Construit le panneau de l'énigme 2.
     *
     * @param ecouteur l'écouteur de réponse (le Moteur) ; ne doit pas être null.
     */
    public Enigme2(EcouteurReponse ecouteur) {
        this.ecouteur = ecouteur;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        // Chargement de l'image de fond depuis les ressources du classpath
        var urlImage = getClass().getResource("enigme2.png");
        if (urlImage != null) {
            imageFond = new ImageIcon(urlImage).getImage();
        }

        var police = new Font("Serif", Font.BOLD, 16);

        // --- Boutons de choix ---
        boutonRoman  = new JButton("Roman Bridger");
        boutonBilly  = new JButton(BONNE_REPONSE);
        boutonMickey = new JButton("Mickey Altieri");

        styliserBouton(boutonRoman,  police);
        styliserBouton(boutonBilly,  police);
        styliserBouton(boutonMickey, police);

        boutonRoman.addActionListener(this);
        boutonBilly.addActionListener(this);
        boutonMickey.addActionListener(this);

        // --- Label tentatives ---
        labelTentatives = new JLabel("Tentatives restantes : " + tentativesRestantes, JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(police);

        // --- Panel image avec positionnement absolu ---
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

        // --- Consigne en bas ---
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

    /**
     * Applique le style gothique commun (fond noir, texte blanc, bordure claire)
     * à un bouton.
     *
     * @param b      le bouton à styliser.
     * @param police la police à appliquer.
     */
    private void styliserBouton(JButton b, Font police) {
        b.setFont(police);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setOpaque(true);
    }

    /**
     * Gère le clic sur l'un des trois boutons de réponse.
     *
     * <p>Si Billy Loomis est sélectionné, délègue à l'écouteur avec la bonne réponse.
     * Sinon, décrémente le compteur de tentatives. Quand les tentatives tombent
     * à 0, délègue à l'écouteur avec le texte du bouton cliqué → end_lose.
     *
     * @param e l'événement d'action déclenché par le bouton cliqué.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boutonClique = (JButton) e.getSource();
        String reponse = boutonClique.getText();

        if (boutonClique == boutonBilly) {
            JOptionPane.showMessageDialog(this, "Bonne réponse !", "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            ecouteur.onReponse(reponse);
        } else {
            tentativesRestantes--;
            labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
            if (tentativesRestantes > 0) {
                JOptionPane.showMessageDialog(this, "Mauvaise réponse... Réessayez !",
                        "Erreur", JOptionPane.WARNING_MESSAGE);
            } else {
                boutonRoman.setEnabled(false);
                boutonBilly.setEnabled(false);
                boutonMickey.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "La réponse était : " + BONNE_REPONSE,
                        "Échec", JOptionPane.ERROR_MESSAGE);
                ecouteur.onReponse(reponse); // route → end_lose
            }
        }
    }
}

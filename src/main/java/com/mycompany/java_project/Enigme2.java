package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Enigme 2 : QCM — identifier le meurtrier dans le film Scream (1996).
 *
 * <p>Règles :
 * <ul>
 *   <li>3 choix possibles : Roman Bridger, Billy Loomis (correct), Mickey Altieri.</li>
 *   <li>2 tentatives maximum.</li>
 *   <li>Bonne réponse → le Moteur avance via onReponse("Billy Loomis") → énigme 3.</li>
 *   <li>Plus de tentatives → le Moteur avance via onReponse du mauvais choix → end_lose.</li>
 * </ul>
 *
 * <p>S'intègre dans l'architecture Moteur/Puzzle/Scenario :
 * elle ne navigue pas elle-même mais délègue au {@link Moteur} via {@code moteur.onReponse()}.
 *
 * @author Lou-Ann
 */
public class Enigme2 extends JPanel implements ActionListener {

    /** Moteur de jeu qui gère la navigation entre les énigmes. */
    private final Moteur moteur;

    /** Puzzle courant chargé depuis le manifest. */
    private final Puzzle puzzle;

    private JButton[] boutons;
    private JLabel labelTentatives;

    /** Nombre de tentatives restantes, initialisé à 2. */
    private int tentativesRestantes;

    /** Texte exact de la bonne réponse, doit correspondre à une clé dans le manifest. */
    static final String BONNE_REPONSE = "Billy Loomis";

    private Image imageFond;

    /**
     * Construit le panneau de l'énigme 2.
     * Ce constructeur suit la même signature que PanelQcm pour
     * pouvoir être instancié par le Moteur.
     *
     * @param puzzle          le puzzle chargé depuis le manifest ; ne doit pas être null.
     * @param dossierScenario chemin vers le dossier du scénario (pour charger l'image).
     * @param moteur          le moteur de jeu, utilisé pour la navigation ; ne doit pas être null.
     */
    public Enigme2(Puzzle puzzle, String dossierScenario, Moteur moteur) {
        this.moteur = moteur;
        this.puzzle = puzzle;
        this.tentativesRestantes = 2;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        // Chargement de l'image depuis le dossier scénario (comme PanelQcm)
        File imgFile = new File(dossierScenario, puzzle.getImage());
        if (imgFile.exists()) {
            imageFond = new ImageIcon(imgFile.getAbsolutePath()).getImage();
        }

        var police = new Font("Serif", Font.BOLD, 16);

        // --- Boutons générés depuis les choix du manifest ---
        boutons = new JButton[puzzle.getChoices().size()];
        for (int i = 0; i < puzzle.getChoices().size(); i++) {
            JButton b = new JButton(puzzle.getChoices().get(i));
            styliserBouton(b, police);
            b.addActionListener(this);
            boutons[i] = b;
        }

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
                int btnW = 180, btnH = 40;
                int n = boutons.length;
                int esp = 20;
                int xDebut = (w - n * btnW - (n - 1) * esp) / 2;
                int y = h - btnH - 20;
                for (int i = 0; i < n; i++) {
                    boutons[i].setBounds(xDebut + i * (btnW + esp), y, btnW, btnH);
                }
                labelTentatives.setBounds(0, y - 30, w, 25);
            }
        };
        panelImage.setBackground(Color.BLACK);
        panelImage.setPreferredSize(new Dimension(800, 480));
        for (JButton b : boutons) panelImage.add(b);
        panelImage.add(labelTentatives);
        this.add(panelImage, BorderLayout.CENTER);

        // --- Consigne lue depuis le manifest ---
        var labelConsigne = new JLabel(
            "<html><div style='text-align:center;'>" + puzzle.getPrompt() + "</div></html>"
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
     * Gère le clic sur l'un des boutons de réponse.
     *
     * <p>Si Billy Loomis est sélectionné, délègue au Moteur avec la bonne réponse.
     * Sinon, décrémente le compteur. Quand les tentatives tombent à 0,
     * délègue au Moteur avec le texte du bouton cliqué → end_lose via le manifest.
     *
     * @param e l'événement d'action déclenché par le bouton cliqué.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boutonClique = (JButton) e.getSource();
        String reponse = boutonClique.getText();

        if (BONNE_REPONSE.equals(reponse)) {
            JOptionPane.showMessageDialog(this, "Bonne réponse !", "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            moteur.onReponse(reponse);
        } else {
            tentativesRestantes--;
            labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
            if (tentativesRestantes > 0) {
                JOptionPane.showMessageDialog(this, "Mauvaise réponse... Réessayez !",
                        "Erreur", JOptionPane.WARNING_MESSAGE);
            } else {
                // Plus de tentatives : désactivation de tous les boutons
                for (JButton b : boutons) b.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "La réponse était : " + BONNE_REPONSE,
                        "Échec", JOptionPane.ERROR_MESSAGE);
                moteur.onReponse(reponse); // route de la mauvaise réponse → end_lose
            }
        }
    }
}

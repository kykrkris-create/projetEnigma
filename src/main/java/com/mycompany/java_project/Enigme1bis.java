package com.mycompany.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Enigme 1bis : trouver la valeur de pi avec 4 chiffres après la virgule.
 * Cette énigme est atteinte si le joueur a choisi la mauvaise clé à l'énigme 1.
 *
 * <p>Règles :
 * <ul>
 *   <li>3 tentatives maximum.</li>
 *   <li>Bonne réponse (3,1415 ou 3.1415) → le Moteur avance via onReponse("3,1415").</li>
 *   <li>Plus de tentatives → le Moteur avance via onReponse("*") → end_lose.</li>
 * </ul>
 *
 * <p>S'intègre dans l'architecture Moteur/Puzzle/Scenario :
 * elle ne navigue pas elle-même mais délègue au {@link Moteur} via {@code moteur.onReponse()}.
 *
 * @author Lou-Ann
 */
public class Enigme1bis extends JPanel implements ActionListener {

    /** Moteur de jeu qui gère la navigation entre les énigmes. */
    private final Moteur moteur;

    /** Puzzle courant chargé depuis le manifest. */
    private final Puzzle puzzle;

    private JTextField champReponse;
    private JButton boutonValider;
    private JLabel labelTentatives;

    /** Nombre de tentatives restantes, initialisé à 3. */
    private int tentativesRestantes = 3;

    /** Bonne réponse acceptée avec une virgule (format français). */
    static final String BONNE_REPONSE_VIRGULE = "3,1415";

    /** Bonne réponse acceptée avec un point (format international). */
    static final String BONNE_REPONSE_POINT = "3.1415";

    private Image imageFond;

    /**
     * Construit le panneau de l'énigme 1bis.
     * Ce constructeur suit la même signature que PanelTexte pour
     * pouvoir être instancié par le Moteur.
     *
     * @param puzzle          le puzzle chargé depuis le manifest ; ne doit pas être null.
     * @param dossierScenario chemin vers le dossier du scénario (pour charger l'image).
     * @param moteur          le moteur de jeu, utilisé pour la navigation ; ne doit pas être null.
     * @param tentativesMax   nombre de tentatives autorisées (passé par le Moteur, normalement 3).
     */
    public Enigme1bis(Puzzle puzzle, String dossierScenario, Moteur moteur, int tentativesMax) {
        this.moteur = moteur;
        this.puzzle = puzzle;
        this.tentativesRestantes = tentativesMax;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        // Chargement de l'image depuis le dossier scénario (comme PanelTexte)
        File imgFile = new File(dossierScenario, puzzle.getImage());
        if (imgFile.exists()) {
            imageFond = new ImageIcon(imgFile.getAbsolutePath()).getImage();
        }

        var police = new Font("Serif", Font.BOLD, 16);

        // --- Champ de saisie ---
        champReponse = new JTextField();
        champReponse.setFont(police);
        champReponse.setBackground(Color.BLACK);
        champReponse.setForeground(Color.WHITE);
        champReponse.setCaretColor(Color.WHITE);
        champReponse.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        champReponse.addActionListener(this); // Entrée clavier = valider

        // --- Bouton valider ---
        boutonValider = new JButton("Valider");
        boutonValider.setFont(police);
        boutonValider.setBackground(Color.BLACK);
        boutonValider.setForeground(Color.WHITE);
        boutonValider.setFocusPainted(false);
        boutonValider.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        boutonValider.setOpaque(true);
        boutonValider.addActionListener(this);

        // --- Label tentatives ---
        labelTentatives = new JLabel("Tentatives restantes : " + tentativesRestantes, JLabel.CENTER);
        labelTentatives.setForeground(Color.WHITE);
        labelTentatives.setFont(police);

        // --- Panel image avec positionnement absolu des composants ---
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
        panelImage.add(champReponse);
        panelImage.add(boutonValider);
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
     * Gère le clic sur "Valider" ou la touche Entrée dans le champ de saisie.
     *
     * <p>Si la réponse est correcte, délègue la navigation au Moteur avec la
     * clé "3,1415". Si toutes les tentatives sont épuisées, délègue avec "*"
     * pour atteindre la route par défaut (end_lose dans le manifest).
     *
     * @param e l'événement d'action déclenché par le bouton ou la touche Entrée.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String reponse = champReponse.getText().trim();

        if (estBonneReponse(reponse)) {
            JOptionPane.showMessageDialog(this, "Bonne réponse !", "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            moteur.onReponse(BONNE_REPONSE_VIRGULE); // clé utilisée dans le manifest
        } else {
            tentativesRestantes--;
            labelTentatives.setText("Tentatives restantes : " + tentativesRestantes);
            if (tentativesRestantes > 0) {
                JOptionPane.showMessageDialog(this, "Mauvaise réponse... Réessayez !",
                        "Erreur", JOptionPane.WARNING_MESSAGE);
                champReponse.setText("");
            } else {
                // Plus de tentatives : désactivation de l'interface
                champReponse.setEnabled(false);
                boutonValider.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "La réponse était : " + BONNE_REPONSE_VIRGULE,
                        "Échec", JOptionPane.ERROR_MESSAGE);
                moteur.onReponse("*"); // route par défaut → end_lose
            }
        }
    }

    /**
     * Vérifie si la réponse saisie correspond à la valeur correcte de pi.
     * Accepte la virgule (3,1415) et le point (3.1415).
     *
     * @param reponse la chaîne saisie par l'utilisateur ; ne doit pas être null.
     * @return {@code true} si la réponse est correcte, {@code false} sinon.
     */
    boolean estBonneReponse(String reponse) {
        return BONNE_REPONSE_VIRGULE.equals(reponse) || BONNE_REPONSE_POINT.equals(reponse);
    }
}

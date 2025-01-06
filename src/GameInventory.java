import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameInventory {
    private final JFrame frame; // Fenêtre principale de l'application
    private final JTextField nomChamp; // Champ de texte pour le nom de l'élément
    private final JTextField quantiteChamp; // Champ de texte pour la quantité de l'élément
    private final DefaultListModel<String> modeleInventaire; // Modèle de liste pour stocker les éléments de l'inventaire
    private final JPanel panneauInventaire; // Panneau pour afficher les éléments de l'inventaire
    private JButton boutonSelectionne; // Bouton actuellement sélectionné

    public GameInventory() {
        // Initialisation de la fenêtre principale
        frame = new JFrame("Gestion d'Inventaire");
        frame.setSize(800, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panneau pour les champs d'entrée
        JPanel panneauEntree = new JPanel();
        panneauEntree.setLayout(new FlowLayout());

        // Création des champs de texte et des boutons
        nomChamp = new JTextField(10);
        quantiteChamp = new JTextField(5);
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonSupprimer = new JButton("Supprimer");

        // Initialisation du modèle d'inventaire
        modeleInventaire = new DefaultListModel<>();
        panneauInventaire = new JPanel();
        panneauInventaire.setLayout(new GridLayout(0, 4)); // GridLayout avec 4 colonnes

        // Ajout d'un JScrollPane pour le panneau d'inventaire
        JScrollPane listeScrollPane = new JScrollPane(panneauInventaire);

        // Ajout des composants au panneau d'entrée
        panneauEntree.add(new JLabel("Nom:"));
        panneauEntree.add(nomChamp);
        panneauEntree.add(new JLabel("Quantité:"));
        panneauEntree.add(quantiteChamp);
        panneauEntree.add(boutonAjouter);
        panneauEntree.add(boutonSupprimer);

        // Ajout des panneaux à la fenêtre principale
        frame.add(panneauEntree, BorderLayout.NORTH);
        frame.add(listeScrollPane, BorderLayout.CENTER);

        // Action pour le bouton "Ajouter"
        boutonAjouter.addActionListener(e -> {
            String nom = nomChamp.getText();
            String quantiteTexte = quantiteChamp.getText();

            // Vérification que les champs ne sont pas vides
            if (!nom.isEmpty() && !quantiteTexte.isEmpty()) {
                int quantite = Integer.parseInt(quantiteTexte);
                boolean elementExistant = false;

                // Vérification si l'élément existe déjà dans l'inventaire
                for (int i = 0; i < modeleInventaire.size(); i++) {
                    String element = modeleInventaire.get(i);
                    if (element.startsWith(nom + " (Quantité: ")) {
                        String quantiteActuelleTexte = element.substring(element.indexOf(": ") + 2, element.indexOf(")"));
                        int quantiteActuelle = Integer.parseInt(quantiteActuelleTexte);
                        quantiteActuelle += quantite;
                        modeleInventaire.set(i, nom + " (Quantité: " + quantiteActuelle + ")");
                        mettreAJourPanneauInventaire();
                        elementExistant = true;
                        break;
                    }
                }

                // Si l'élément n'existe pas, l'ajouter à l'inventaire
                if (!elementExistant) {
                    modeleInventaire.addElement(nom + " (Quantité: " + quantite + ")");
                    mettreAJourPanneauInventaire();
                }

                // Réinitialiser les champs de texte
                nomChamp.setText("");
                quantiteChamp.setText("");
            } else {
                // Afficher un message d'erreur si les champs sont vides
                JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action pour le bouton "Supprimer"
        boutonSupprimer.addActionListener(e -> {
            String elementSelectionne = obtenirElementSelectionne();

            // Vérification qu'un élément est sélectionné
            if (elementSelectionne != null) {
                String quantiteActuelleTexte = elementSelectionne.substring(elementSelectionne.indexOf(": ") + 2, elementSelectionne                    .indexOf(")"));
                int quantiteActuelle = Integer.parseInt(quantiteActuelleTexte);
                String quantiteTexte = quantiteChamp.getText();

                // Vérification que la quantité à supprimer est spécifiée
                if (!quantiteTexte.isEmpty()) {
                    int quantiteASupprimer = Integer.parseInt(quantiteTexte);

                    // Vérification que la quantité à supprimer ne dépasse pas la quantité existante
                    if (quantiteASupprimer > quantiteActuelle) {
                        JOptionPane.showMessageDialog(frame, "La quantité à supprimer est supérieure à la quantité existante.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        quantiteActuelle -= quantiteASupprimer;
                        // Mettre à jour ou supprimer l'élément selon la nouvelle quantité
                        if (quantiteActuelle > 0) {
                            modeleInventaire.set(modeleInventaire.indexOf(elementSelectionne), elementSelectionne.substring(0, elementSelectionne.indexOf(": ") + 2) + quantiteActuelle + ")");
                        } else {
                            modeleInventaire.removeElement(elementSelectionne);
                        }
                        mettreAJourPanneauInventaire();
                    }
                } else {
                    // Afficher un message d'erreur si la quantité à supprimer est vide
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer une quantité à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Afficher un message d'erreur si aucun élément n'est sélectionné
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un élément à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    // Méthode pour mettre à jour le panneau d'inventaire
    private void mettreAJourPanneauInventaire() {
        panneauInventaire.removeAll(); // Effacer le panneau d'inventaire

        // Ajouter un bouton pour chaque élément de l'inventaire
        for (int i = 0; i < modeleInventaire.size(); i++) {
            String element = modeleInventaire.get(i);
            JButton boutonElement = new JButton(element);
            boutonElement.setPreferredSize(new Dimension(70, 70)); // Définir une taille fixe pour les boutons (petits carrés)

            // Action pour mettre à jour le champ de quantité lorsque le bouton est cliqué
            boutonElement.addActionListener(e -> {
                // Mettre à jour le champ de quantité avec la quantité de l'élément sélectionné
                String quantiteActuelleTexte = element.substring(element.indexOf(": ") + 2, element.indexOf(")"));
                quantiteChamp.setText(quantiteActuelleTexte);
                selectionnerBouton(boutonElement); // Sélectionner le bouton
            });

            // Ajouter un MouseListener pour gérer la sélection
            boutonElement.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectionnerBouton(boutonElement); // Sélectionner le bouton
                }
            });

            panneauInventaire.add(boutonElement); // Ajouter le bouton au panneau d'inventaire
        }

        panneauInventaire.revalidate(); // Revalider le panneau pour appliquer les changements
        panneauInventaire.repaint(); // Repeindre le panneau pour afficher les nouveaux boutons
    }

    // Méthode pour sélectionner un bouton
    private void selectionnerBouton(JButton bouton) {
        // Réinitialiser la couleur de fond des boutons précédemment sélectionnés
        if (boutonSelectionne != null) {
            boutonSelectionne.setBackground(null); // Réinitialiser la couleur de fond
        }
        // Mettre à jour le bouton sélectionné
        boutonSelectionne = bouton;
        boutonSelectionne.setBackground(Color.LIGHT_GRAY); // Changer la couleur de fond pour indiquer la sélection
    }

    // Méthode pour obtenir l'élément actuellement sélectionné
    private String obtenirElementSelectionne() {
        // Retourne le texte du bouton actuellement sélectionné
        return boutonSelectionne != null ? boutonSelectionne.getText() : null;
    }
}


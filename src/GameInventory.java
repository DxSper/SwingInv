// Import
// Composant graphique
import javax.swing.*;
// Classse des composants dépendant du systeme natif
import java.awt.*;
// Gérer les évenements
import java.awt.event.ActionEvent;
// Ecouter les évenements
import java.awt.event.ActionListener;

// Class GameInventory
public class GameInventory {
    // Déclaration
    // la fenetre principale
    private JFrame frame;
    // Champs nom d'objet
    private JTextField nameField;
    // Champs quantité d'objet
    private JTextField quantityField;
    // Modele de liste par défault
    private DefaultListModel<String> inventoryModel;
    // Liste objet de l'inventaire
    private JList<String> inventoryList;

    
    public GameInventory() {
        // Créer la fenetre principale
        frame = new JFrame("Gestion d'Inventaire");
        // Taille de la fenetre
        frame.setSize(800, 300);
        // Fermer lors d'un clique sur la croix
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Gestionnaire de mise en page
        frame.setLayout(new BorderLayout());

        // Créer les panneaux
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // Créer les composants
        nameField = new JTextField(10); // TextField pour le nom de l'objet
        quantityField = new JTextField(5); // TextField pour la quantité
        JButton addButton = new JButton("Ajouter"); // Bouton "Ajouter"
        JButton removeButton = new JButton("Supprimer"); // Bouton "Supprimer"

        // Modèle et liste pour l'inventaire
        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        JScrollPane listScrollPane = new JScrollPane(inventoryList); // Ajout d'un JScrollPane pour la liste

        // Ajouter les composants au panneau d'entrée
        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Quantité:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        // Ajouter les panneaux à la fenetre principale
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listScrollPane, BorderLayout.CENTER);

        // Action pour le bouton "Ajouter"
        addButton.addActionListener(new ActionListener() {
            /**
            * L'annotation @Override indique que la méthode actionPerformed 
            implémente une méthode de l'interface ActionListener,
            permettant ainsi une vérification par le compilateur
            et améliorant la lisibilité du code
             */
            @Override 
            public void actionPerformed(ActionEvent e) {
                // Récuperer le nom de l'objet
                String name = nameField.getText();
                // Récuperer la quantité de l'objet
                String quantityText = quantityField.getText();

                // Vérifier que les champs ne sont pas vides
                if (!name.isEmpty() && !quantityText.isEmpty()) {
                    int quantity = Integer.parseInt(quantityText);
                    boolean itemExists = false;

                    // Vérifier si l'objet existe déjà dans l'inventaire
                    for (int i = 0; i < inventoryModel.size(); i++) {
                        String item = inventoryModel.get(i);
                        if (item.startsWith(name + " (Quantité: ")) {
                            // Si l'objet existe, augmenter la quantité
                            String currentQuantityText = item.substring(item.indexOf(": ") + 2, item.indexOf(")"));
                            int currentQuantity = Integer.parseInt(currentQuantityText);
                            currentQuantity += quantity; // Augmenter la quantité
                            inventoryModel.set(i, name + " (Quantité: " + currentQuantity + ")");
                            itemExists = true;
                            break;
                        }
                    }

                    // Si l'objet n'existe pas, l'ajouter à l'inventaire
                    if (!itemExists) {
                        inventoryModel.addElement(name + " (Quantité: " + quantity + ")");
                    }

                    // Réinitialiser les champs de saisie
                    nameField.setText("");
                    quantityField.setText("");
                } else {
                    // Afficher un message d'erreur si les champs sont vides
                    JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action pour le bouton "Supprimer"
        removeButton.addActionListener(new ActionListener() {
            /**
            * actionPerformed implémente la methode de l'interface ActionListener
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récuperer l'index de liste de l'objet sélectionné
                int selectedIndex = inventoryList.getSelectedIndex();
                // Vérifier qu'un élément est sélectionné
                if (selectedIndex != -1) {
                    // Récuperer l'index de la liste de l'objet sélectionné (en string)
                    String selectedItem = inventoryModel.get(selectedIndex);
                    // Récuperer la quantité de l'objet séléctionné (en string)
                    String currentQuantityText = selectedItem.substring(selectedItem.indexOf(": ") + 2, selectedItem.indexOf(")"));
                    // Transformer la quantité string en Integer
                    int currentQuantity = Integer.parseInt(currentQuantityText);

                    // Récupérer la quantité à supprimer dans le champs de texte (en string)
                    String quantityText = quantityField.getText();
                    // Vérifier si la quantité n'est pas vide
                    if (!quantityText.isEmpty()) {
                        // Transformer la quantité string en Integer
                        int quantityToRemove = Integer.parseInt(quantityText);

                        // Vérifier si la quantité à supprimer est valide
                        if (quantityToRemove > currentQuantity) {
                            // Afficher que la quantité à supprimé > existante
                            JOptionPane.showMessageDialog(frame, "La quantité à supprimer est supérieure à la quantité existante.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Mettre à jour la quantité ou supprimer l'élément si la quantité atteint zéro
                            currentQuantity -= quantityToRemove;
                            if (currentQuantity > 0) {
                                inventoryModel.set(selectedIndex, selectedItem.substring(0, selectedItem.indexOf(": ") + 2) + currentQuantity + ")");
                            } else {
                                inventoryModel.remove(selectedIndex); // Supprimer l'élément si la quantité est zéro
                            }
                        }
                    } else {
                        // Afficher un message d'erreur si la quantité est vide
                        JOptionPane.showMessageDialog(frame, "Veuillez entrer une quantité à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Afficher un message d'erreur si aucun élément n'est sélectionné
                    JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un élément à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Rendre la fenetre visible
        frame.setVisible(true);
    }
}
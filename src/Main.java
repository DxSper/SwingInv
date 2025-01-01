import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Démarrer l'application d'inventaire
        SwingUtilities.invokeLater(() -> new GameInventory());
    }
}

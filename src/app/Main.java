package app;

import app.database.DatabaseInitializer;
import app.ui.ReservarCaronaUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Cria as tabelas no banco, se não existirem
        DatabaseInitializer.criarTabelas();

        // Abre a interface gráfica na thread do Swing
        SwingUtilities.invokeLater(() -> {
            new ReservarCaronaUI().setVisible(true);
        });
    }
}
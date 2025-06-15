package app.ui;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Carona Solidária");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton cadastrarBtn = new JButton("Cadastrar Carona");
        JButton listarBtn = new JButton("Listar Caronas");
        JButton sairBtn = new JButton("Sair");

        cadastrarBtn.addActionListener(e -> {
            dispose();
            new CadastroCaronaUI().setVisible(true);
        });

        listarBtn.addActionListener(e -> {
            dispose();
            new ListarCaronasUI().setVisible(true);
        });

        sairBtn.addActionListener(e -> System.exit(0));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(cadastrarBtn);
        panel.add(listarBtn);
        panel.add(sairBtn);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
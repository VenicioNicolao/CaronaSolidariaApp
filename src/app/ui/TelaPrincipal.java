package app.ui;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Carona Solidária");
        setSize(350, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton cadastrarCaronaBtn = new JButton("Cadastrar Carona");
        JButton listarBtn = new JButton("Listar Caronas");
        JButton reservarCaronaBtn = new JButton("Reservar Carona");
        JButton minhasReservasBtn = new JButton("Minhas Reservas");
        JButton sairBtn = new JButton("Sair");

        cadastrarCaronaBtn.addActionListener(e -> {
            dispose();
            new CadastroCaronaUI().setVisible(true);
        });

        listarBtn.addActionListener(e -> {
            dispose();
            new ListarCaronasUI().setVisible(true);
        });

        reservarCaronaBtn.addActionListener(e -> {
            dispose();
            new ReservarCaronaUI().setVisible(true);
        });

        minhasReservasBtn.addActionListener(e -> {
            dispose();
            new MinhasReservasUI().setVisible(true);
        });

        sairBtn.addActionListener(e -> System.exit(0));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(cadastrarCaronaBtn);
        panel.add(listarBtn);
        panel.add(reservarCaronaBtn);
        panel.add(minhasReservasBtn);
        panel.add(sairBtn);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
package app.ui;

import app.database.DatabaseInitializer;
import app.auth.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MinhasReservasUI extends JFrame {

    private JTable tabelaReservas;
    private DefaultTableModel modelo;

    public MinhasReservasUI() {
        setTitle("Minhas Reservas");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {"ID", "Origem", "Destino", "Motorista", "Horário", "Número de Vagas"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaReservas = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabelaReservas);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            new TelaPrincipal().setVisible(true);
        });

        JPanel painelInferior = new JPanel();
        painelInferior.add(btnVoltar);

        add(scrollPane, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        carregarReservas();
    }

    private void carregarReservas() {
        String usuarioLogado = SessaoUsuario.getLogin();
        List<HashMap<String, String>> reservas = DatabaseInitializer.listarReservas(usuarioLogado);
        modelo.setRowCount(0);
        for (HashMap<String, String> reserva : reservas) {
            Object[] linha = {
                    reserva.get("id"),
                    reserva.get("origem"),
                    reserva.get("destino"),
                    reserva.get("motorista"),
                    reserva.get("horario"),
                    reserva.get("vagas")
            };
            modelo.addRow(linha);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MinhasReservasUI().setVisible(true));
    }
}
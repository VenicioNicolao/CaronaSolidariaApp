package app.ui;

import app.database.DatabaseInitializer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReservarCaronaUI extends JFrame {

    private JTable tabelaCaronas;
    private DefaultTableModel modeloTabela;

    public ReservarCaronaUI() {
        setTitle("Reservar Carona");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {"ID", "Motorista", "Origem", "Destino", "Horário", "Vagas"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaCaronas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaCaronas);

        JButton btnReservar = new JButton("Reservar Carona");
        btnReservar.addActionListener(e -> reservarCarona());

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            new TelaPrincipal().setVisible(true);
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(btnReservar);
        painelBotoes.add(btnVoltar);

        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarCaronasDisponiveis();
    }

    private void carregarCaronasDisponiveis() {
        modeloTabela.setRowCount(0);
        String sql = "SELECT * FROM carona WHERE vagas > 0";

        try (Connection conn = DatabaseInitializer.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("motorista"),
                        rs.getString("origem"),
                        rs.getString("destino"),
                        rs.getString("horario"),
                        rs.getInt("vagas")
                };
                modeloTabela.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar caronas: " + e.getMessage());
        }
    }

    private void reservarCarona() {
        int linhaSelecionada = tabelaCaronas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma carona para reservar.");
            return;
        }

        int idCarona = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        int novasVagas = DatabaseInitializer.reservarCarona(idCarona);

        if (novasVagas >= 0) {
            JOptionPane.showMessageDialog(this, "Reserva realizada com sucesso!");

            if (novasVagas == 0) {
                modeloTabela.removeRow(linhaSelecionada); // remove da tabela
            } else {
                modeloTabela.setValueAt(novasVagas, linhaSelecionada, 5); // atualiza coluna "Vagas"
            }

        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível reservar a carona. Verifique as vagas e tente novamente.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservarCaronaUI().setVisible(true));
    }
}
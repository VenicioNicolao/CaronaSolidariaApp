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
    private static MinhasReservasUI instancia;

    public MinhasReservasUI() {
        setTitle("Minhas Reservas");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        instancia = this;

        // REMOVIDA a coluna "Número de Vagas"
        String[] colunas = {"ID", "Origem", "Destino", "Motorista", "Horário"};
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
            instancia = null;
            dispose();
            new TelaPrincipal().setVisible(true);
        });

        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.addActionListener(e -> cancelarReserva());

        JPanel painelInferior = new JPanel();
        painelInferior.add(btnCancelar);
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
                    reserva.get("horario")
                    // NÃO adicionamos mais vaga aqui
            };
            modelo.addRow(linha);
        }
    }

    private void cancelarReserva() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar esta reserva?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        int idReserva = Integer.parseInt(modelo.getValueAt(linhaSelecionada, 0).toString());

        boolean sucesso = DatabaseInitializer.cancelarReserva(idReserva);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso.");
            carregarReservas();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cancelar reserva.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para atualizar vagas pode ser removido ou mantido, pois não mostra vagas na tabela
    public static void atualizarVagasCarona(int idCarona, int novasVagas) {
        if (instancia == null) return;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MinhasReservasUI().setVisible(true));
    }
}
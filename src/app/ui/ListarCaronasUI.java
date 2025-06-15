package app.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ListarCaronasUI extends JFrame {

    private DefaultTableModel model;
    private JTable tabela;
    private JTextField filtroOrigem;
    private JTextField filtroDestino;
    private JTextField filtroHorario;

    public ListarCaronasUI() {
        setTitle("Lista de Caronas");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelFiltro = new JPanel(new FlowLayout());

        painelFiltro.add(new JLabel("Origem:"));
        filtroOrigem = new JTextField(10);
        painelFiltro.add(filtroOrigem);

        painelFiltro.add(new JLabel("Destino:"));
        filtroDestino = new JTextField(10);
        painelFiltro.add(filtroDestino);

        painelFiltro.add(new JLabel("Horário:"));
        filtroHorario = new JTextField(8);
        painelFiltro.add(filtroHorario);

        JButton btnBuscar = new JButton("Buscar");
        painelFiltro.add(btnBuscar);

        add(painelFiltro, BorderLayout.NORTH);

        String[] colunas = {"ID", "Motorista", "Origem", "Destino", "Horário", "Vagas"};
        model = new DefaultTableModel(colunas, 0);
        tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExcluir = new JButton("Excluir");
        JButton btnVoltar = new JButton("Voltar");

        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        carregarCaronas("", "", "");

        btnBuscar.addActionListener(e -> {
            String origem = filtroOrigem.getText().trim();
            String destino = filtroDestino.getText().trim();
            String horario = filtroHorario.getText().trim();
            carregarCaronas(origem, destino, horario);
        });

        btnExcluir.addActionListener(e -> excluirCarona());

        btnVoltar.addActionListener(e -> {
            dispose();
            new TelaPrincipal().setVisible(true);
        });
    }

    private void carregarCaronas(String origemFiltro, String destinoFiltro, String horarioFiltro) {
        model.setRowCount(0);

        String sql = "SELECT * FROM carona WHERE 1=1";
        if (!origemFiltro.isEmpty()) sql += " AND origem LIKE ?";
        if (!destinoFiltro.isEmpty()) sql += " AND destino LIKE ?";
        if (!horarioFiltro.isEmpty()) sql += " AND horario LIKE ?";

        try (Connection conn = app.database.DatabaseInitializer.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            if (!origemFiltro.isEmpty()) pstmt.setString(paramIndex++, "%" + origemFiltro + "%");
            if (!destinoFiltro.isEmpty()) pstmt.setString(paramIndex++, "%" + destinoFiltro + "%");
            if (!horarioFiltro.isEmpty()) pstmt.setString(paramIndex++, "%" + horarioFiltro + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("motorista"),
                        rs.getString("origem"),
                        rs.getString("destino"),
                        rs.getString("horario"),
                        rs.getInt("vagas")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar caronas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCarona() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma carona para excluir.");
            return;
        }

        int id = (int) model.getValueAt(linhaSelecionada, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a carona ID " + id + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = app.database.DatabaseInitializer.connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM carona WHERE id = ?")) {

                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Carona excluída com sucesso.");
                carregarCaronas("", "", "");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir carona: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListarCaronasUI().setVisible(true));
    }
}
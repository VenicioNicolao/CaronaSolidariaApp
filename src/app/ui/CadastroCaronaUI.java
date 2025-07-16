package app.ui;

import app.database.DatabaseInitializer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CadastroCaronaUI extends JFrame {

    private JTextField motoristaField, origemField, destinoField, horarioField, vagasField;

    public CadastroCaronaUI() {
        setTitle("Cadastrar Carona");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Motorista:"));
        motoristaField = new JTextField();
        panel.add(motoristaField);

        panel.add(new JLabel("Origem:"));
        origemField = new JTextField();
        panel.add(origemField);

        panel.add(new JLabel("Destino:"));
        destinoField = new JTextField();
        panel.add(destinoField);

        panel.add(new JLabel("Horário:"));
        horarioField = new JTextField();
        panel.add(horarioField);

        panel.add(new JLabel("Vagas:"));
        vagasField = new JTextField();
        panel.add(vagasField);

        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> salvarCarona());
        panel.add(new JLabel());
        panel.add(salvarBtn);

        add(panel);
    }

    private void salvarCarona() {
        String motorista = motoristaField.getText().trim();
        String origem = origemField.getText().trim();
        String destino = destinoField.getText().trim();
        String horario = horarioField.getText().trim();
        String vagasText = vagasField.getText().trim();

        if (motorista.isEmpty() || origem.isEmpty() || destino.isEmpty() || horario.isEmpty() || vagasText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        int vagas;
        try {
            vagas = Integer.parseInt(vagasText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vagas deve ser um número inteiro.");
            return;
        }

        try (Connection conn = DatabaseInitializer.connect()) {
            String sql = "INSERT INTO carona (motorista, origem, destino, horario, vagas) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, motorista);
            stmt.setString(2, origem);
            stmt.setString(3, destino);
            stmt.setString(4, horario);
            stmt.setInt(5, vagas);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Carona cadastrada com sucesso.");
            dispose();

            SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar carona: " + ex.getMessage());
        }
    }
}
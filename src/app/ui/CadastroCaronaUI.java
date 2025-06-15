package app.ui;

import javax.swing.*;
import java.awt.*;

public class CadastroCaronaUI extends JFrame {

    private JTextField motoristaField;
    private JTextField origemField;
    private JTextField destinoField;
    private JTextField horarioField;

    public CadastroCaronaUI() {
        setTitle("Cadastro de Carona");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

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

        JButton salvarButton = new JButton("Salvar");
        JButton voltarButton = new JButton("Voltar");

        panel.add(salvarButton);
        panel.add(voltarButton);

        add(panel);

        salvarButton.addActionListener(e -> salvarCarona());

        voltarButton.addActionListener(e -> {
            dispose();
            new TelaPrincipal().setVisible(true);
        });
    }

    private void salvarCarona() {
        String motorista = motoristaField.getText();
        String origem = origemField.getText();
        String destino = destinoField.getText();
        String horario = horarioField.getText();

        if (motorista.isEmpty() || origem.isEmpty() || destino.isEmpty() || horario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO carona(motorista, origem, destino, horario) VALUES (?, ?, ?, ?)";

        try (var conn = app.database.DatabaseInitializer.connect()) {
            try (var pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, motorista);
                pstmt.setString(2, origem);
                pstmt.setString(3, destino);
                pstmt.setString(4, horario);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Carona cadastrada com sucesso!");

                motoristaField.setText("");
                origemField.setText("");
                destinoField.setText("");
                horarioField.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar carona: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
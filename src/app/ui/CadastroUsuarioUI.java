package app.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.database.DatabaseInitializer;

public class CadastroUsuarioUI extends JFrame {

    public CadastroUsuarioUI() {
        setTitle("Cadastro de Usuário");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nomeField = new JTextField();
        JTextField loginField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JButton cadastrarBtn = new JButton("Cadastrar");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        panel.add(new JLabel());
        panel.add(cadastrarBtn);

        add(panel);

        cadastrarBtn.addActionListener(e -> {
            String nome = nomeField.getText();
            String login = loginField.getText();
            String senha = new String(senhaField.getPassword());

            try (Connection conn = DatabaseInitializer.connect()) {
                String sql = "INSERT INTO usuario (nome, login, senha) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nome);
                stmt.setString(2, login);
                stmt.setString(3, senha);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroUsuarioUI().setVisible(true));
    }
}
package app.ui;

import app.database.DatabaseInitializer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CadastroUsuarioUI extends JFrame {

    private JTextField nomeField;
    private JTextField loginField;
    private JPasswordField senhaField;

    public CadastroUsuarioUI() {
        setTitle("Cadastro de Usuário");
        setSize(350, 220);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Login:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.addActionListener(e -> cadastrarUsuario());

        panel.add(new JLabel());
        panel.add(cadastrarBtn);

        add(panel);
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText().trim();
        String login = loginField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        try (Connection conn = DatabaseInitializer.connect()) {
            String sql = "INSERT INTO usuario (nome, login, senha) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroUsuarioUI().setVisible(true));
    }
}
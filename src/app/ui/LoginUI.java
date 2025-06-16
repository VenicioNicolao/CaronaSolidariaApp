package app.ui;

import app.database.DatabaseInitializer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginUI extends JFrame {

    private JTextField loginField;
    private JPasswordField senhaField;

    public LoginUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Login:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        JButton entrarBtn = new JButton("Entrar");
        entrarBtn.addActionListener(e -> autenticar());

        panel.add(new JLabel()); // espaço vazio
        panel.add(entrarBtn);

        add(panel);
    }

    private void autenticar() {
        String login = loginField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        try (Connection conn = DatabaseInitializer.connect()) {
            String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dispose();
                new TelaPrincipal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
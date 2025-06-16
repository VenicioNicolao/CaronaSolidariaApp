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
        setSize(320, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        fieldsPanel.add(new JLabel("Login:"));
        loginField = new JTextField();
        fieldsPanel.add(loginField);

        fieldsPanel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        fieldsPanel.add(senhaField);

        mainPanel.add(fieldsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton entrarBtn = new JButton("Entrar");
        entrarBtn.setPreferredSize(new Dimension(100, 30));
        entrarBtn.addActionListener(e -> autenticar());
        buttonsPanel.add(entrarBtn);

        JButton criarContaBtn = new JButton("Criar conta");
        criarContaBtn.setPreferredSize(new Dimension(100, 30));
        criarContaBtn.addActionListener(e -> new CadastroUsuarioUI().setVisible(true));
        buttonsPanel.add(criarContaBtn);

        mainPanel.add(buttonsPanel);

        add(mainPanel);
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
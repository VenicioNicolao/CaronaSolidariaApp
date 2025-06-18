package app.ui;

import app.database.DatabaseInitializer;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private JTextField loginField = new JTextField();
    private JPasswordField senhaField = new JPasswordField();

    public LoginUI() {
        setTitle("Login");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Login:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginField.setPreferredSize(new Dimension(150, 25));
        painel.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        senhaField.setPreferredSize(new Dimension(150, 25));
        painel.add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setPreferredSize(new Dimension(120, 30));
        painel.add(btnEntrar, gbc);

        gbc.gridy = 3;
        JButton btnCriar = new JButton("Criar Usuário");
        btnCriar.setPreferredSize(new Dimension(120, 30));
        painel.add(btnCriar, gbc);

        btnEntrar.addActionListener(e -> autenticar());

        btnCriar.addActionListener(e -> {
            dispose();
            new CadastroUsuarioUI().setVisible(true);
        });

        add(painel);
        setVisible(true);
    }

    private void autenticar() {
        String login = loginField.getText().trim();
        String senha = new String(senhaField.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha login e senha.");
            return;
        }

        String sql = "SELECT * FROM usuario WHERE login=? AND senha=?";

        try (Connection conn = DatabaseInitializer.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login efetuado com sucesso!");
                    dispose();
                    new TelaPrincipal().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Login ou senha inválidos.");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
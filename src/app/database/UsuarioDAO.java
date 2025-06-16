package app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsuarioDAO {

    public static void cadastrarUsuario(String nome, String login, String senha) {
        String sql = "INSERT INTO usuario(nome, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseInitializer.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);

            stmt.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Aqui você define os dados do usuário que será cadastrado
        cadastrarUsuario("João Pedro", "joaopedro", "1234");
    }
}
package app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static boolean validarLogin(String login, String senha) {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
        try (Connection conn = DatabaseInitializer.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, senha);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
            return false;
        }
    }

    public static boolean cadastrarUsuario(String nome, String login, String senha) {
        String sql = "INSERT INTO usuario (nome, login, senha) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseInitializer.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, login);
            pstmt.setString(3, senha);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }
}
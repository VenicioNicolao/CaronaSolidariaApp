package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CriarTabelaCaronas {
    private static final String DB_URL = "jdbc:sqlite:carona.db";

    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS caronas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome_motorista TEXT NOT NULL,
                telefone_motorista TEXT NOT NULL,
                origem TEXT NOT NULL,
                destino TEXT NOT NULL,
                horario TEXT NOT NULL
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'caronas' criada com sucesso (ou já existia).");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        criarTabela();
    }
}
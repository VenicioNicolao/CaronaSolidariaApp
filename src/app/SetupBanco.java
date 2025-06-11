package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SetupBanco {
    private static final String DB_URL = "jdbc:sqlite:carona.db";

    public static void main(String[] args) {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                telefone TEXT NOT NULL
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'usuarios' criada com sucesso (ou já existia).");

        } catch (Exception e) {
            System.err.println("Erro ao criar tabela 'usuarios': " + e.getMessage());
        }
    }
}
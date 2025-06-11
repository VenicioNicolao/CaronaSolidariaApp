package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:carona.db";

    public static void main(String[] args) {
        initializeDatabase();
    }

    public static void initializeDatabase() {
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

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            System.out.println("Tabela 'caronas' verificada com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
        }
    }
}

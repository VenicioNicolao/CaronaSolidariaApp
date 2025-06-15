package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String URL = "jdbc:sqlite:carona.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void criarTabelas() {
        String sqlCarona = "CREATE TABLE IF NOT EXISTS carona ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "motorista TEXT NOT NULL,"
                + "origem TEXT NOT NULL,"
                + "destino TEXT NOT NULL,"
                + "horario TEXT NOT NULL,"
                + "vagas INTEGER NOT NULL"
                + ");";

        String sqlPassageiro = "CREATE TABLE IF NOT EXISTS passageiro ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "telefone TEXT NOT NULL,"
                + "carona_id INTEGER,"
                + "FOREIGN KEY(carona_id) REFERENCES carona(id)"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCarona);
            stmt.execute(sqlPassageiro);
            System.out.println("Tabelas criadas com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static void adicionarColunaVagas() {
        String sql = "ALTER TABLE carona ADD COLUMN vagas INTEGER DEFAULT 1";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Coluna 'vagas' adicionada com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate column name") || e.getMessage().contains("already exists")) {
                System.out.println("A coluna 'vagas' já existe.");
            } else {
                System.out.println("Erro ao adicionar coluna: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        criarTabelas();
        adicionarColunaVagas(); // execute uma vez se for banco antigo
    }
}
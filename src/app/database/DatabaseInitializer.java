package app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "login TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL"
                + ");";

        String sqlReserva = "CREATE TABLE IF NOT EXISTS reserva ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "carona_id INTEGER NOT NULL,"
                + "FOREIGN KEY(carona_id) REFERENCES carona(id)"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCarona);
            stmt.execute(sqlUsuario);
            stmt.execute(sqlReserva);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static void ajustarTabelaReserva() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS reserva_nova ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "carona_id INTEGER NOT NULL,"
                + "FOREIGN KEY(carona_id) REFERENCES carona(id)"
                + ");";

        String sqlCopy = "INSERT INTO reserva_nova (id, carona_id) "
                + "SELECT id, carona_id FROM reserva;";

        String sqlDrop = "DROP TABLE reserva;";
        String sqlRename = "ALTER TABLE reserva_nova RENAME TO reserva;";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            stmt.execute(sqlCreate);
            stmt.execute(sqlCopy);
            stmt.execute(sqlDrop);
            stmt.execute(sqlRename);
            conn.commit();

            System.out.println("Tabela 'reserva' ajustada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao ajustar tabela 'reserva'.");
        }
    }

    public static int reservarCarona(int caronaId) {
        String sqlUpdate = "UPDATE carona SET vagas = vagas - 1 WHERE id = ? AND vagas > 0";
        String sqlInsert = "INSERT INTO reserva (carona_id) VALUES (?)";
        String sqlSelect = "SELECT vagas FROM carona WHERE id = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, caronaId);
                int linhasAfetadas = psUpdate.executeUpdate();
                if (linhasAfetadas == 0) {
                    conn.rollback();
                    return -1;
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, caronaId);
                psInsert.executeUpdate();
            }

            conn.commit();

            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, caronaId);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("vagas");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static List<HashMap<String, String>> listarReservas(String loginUsuario) {
        List<HashMap<String, String>> reservas = new ArrayList<>();

        String sql = "SELECT r.id, c.origem, c.destino, c.horario, c.motorista, c.vagas "
                + "FROM reserva r "
                + "JOIN carona c ON r.carona_id = c.id "
                + "ORDER BY r.id DESC";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HashMap<String, String> linha = new HashMap<>();
                linha.put("id", rs.getString("id"));
                linha.put("origem", rs.getString("origem"));
                linha.put("destino", rs.getString("destino"));
                linha.put("horario", rs.getString("horario"));
                linha.put("motorista", rs.getString("motorista"));
                linha.put("vagas", rs.getString("vagas"));
                reservas.add(linha);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    // MÉTODO NOVO PARA CANCELAR RESERVA
    public static boolean cancelarReserva(int idReserva) {
        String sqlCaronaId = "SELECT carona_id FROM reserva WHERE id = ?";
        String sqlDeleteReserva = "DELETE FROM reserva WHERE id = ?";
        String sqlUpdateVagas = "UPDATE carona SET vagas = vagas + 1 WHERE id = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            int caronaId;

            // Buscar o carona_id da reserva
            try (PreparedStatement psCarona = conn.prepareStatement(sqlCaronaId)) {
                psCarona.setInt(1, idReserva);
                try (ResultSet rs = psCarona.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false; // reserva não existe
                    }
                    caronaId = rs.getInt("carona_id");
                }
            }

            // Deletar reserva
            try (PreparedStatement psDelete = conn.prepareStatement(sqlDeleteReserva)) {
                psDelete.setInt(1, idReserva);
                psDelete.executeUpdate();
            }

            // Atualizar vagas da carona
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateVagas)) {
                psUpdate.setInt(1, caronaId);
                psUpdate.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
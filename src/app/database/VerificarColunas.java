package app.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VerificarColunas {

    public static void main(String[] args) {
        try (Connection conn = DatabaseInitializer.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(carona)")) {

            System.out.println("Colunas da tabela 'carona':");
            System.out.println("---------------------------------");
            while (rs.next()) {
                int cid = rs.getInt("cid");
                String nome = rs.getString("name");
                String tipo = rs.getString("type");
                int notnull = rs.getInt("notnull");
                String dflt = rs.getString("dflt_value");
                int pk = rs.getInt("pk");

                System.out.printf("cid: %d | nome: %s | tipo: %s | notnull: %d | default: %s | pk: %d%n",
                        cid, nome, tipo, notnull, dflt, pk);
            }
            System.out.println("---------------------------------");
        } catch (SQLException e) {
            System.err.println("Erro ao verificar colunas: " + e.getMessage());
        }
    }
}
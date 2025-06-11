package app.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AtualizaTabelaCarona {

    private static final String DB_URL = "jdbc:sqlite:carona.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Criar nova tabela
            String sql1 = """
                CREATE TABLE IF NOT EXISTS caronas_nova (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome_motorista TEXT NOT NULL,
                    telefone_motorista TEXT NOT NULL,
                    origem TEXT NOT NULL,
                    destino TEXT NOT NULL,
                    horario TEXT NOT NULL
                );
            """;

            // Copiar dados
            String sql2 = """
                INSERT INTO caronas_nova (nome_motorista, telefone_motorista, origem, destino, horario)
                SELECT nome_motorista, telefone_motorista, origem, destino, horario FROM caronas;
            """;

            // Apagar tabela antiga
            String sql3 = "DROP TABLE caronas;";

            // Renomear tabela nova
            String sql4 = "ALTER TABLE caronas_nova RENAME TO caronas;";

            stmt.executeUpdate(sql1);
            System.out.println("Tabela caronas_nova criada.");

            stmt.executeUpdate(sql2);
            System.out.println("Dados copiados para caronas_nova.");

            stmt.executeUpdate(sql3);
            System.out.println("Tabela caronas antiga apagada.");

            stmt.executeUpdate(sql4);
            System.out.println("Tabela caronas_nova renomeada para caronas.");

            System.out.println("Atualização da tabela concluída com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro na atualização da tabela: " + e.getMessage());
        }
    }
}
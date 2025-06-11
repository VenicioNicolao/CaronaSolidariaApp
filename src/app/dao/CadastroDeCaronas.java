package app.dao;

import app.model.Carona;
import app.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CadastroDeCaronas {

    private static final String DB_URL = "jdbc:sqlite:carona.db";

    public boolean existeCarona(Carona carona) {
        String sql = """
            SELECT COUNT(*) FROM caronas
            WHERE nome_motorista = ? AND telefone_motorista = ?
              AND origem = ? AND destino = ? AND horario = ?
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Usuario motorista = carona.getMotorista();

            pstmt.setString(1, motorista.getNome());
            pstmt.setString(2, motorista.getTelefone());
            pstmt.setString(3, carona.getOrigem());
            pstmt.setString(4, carona.getDestino());
            pstmt.setString(5, carona.getHorario());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar carona: " + e.getMessage());
        }
        return false;
    }

    public void salvar(Carona carona) {
        if (existeCarona(carona)) {
            System.out.println("Carona já existe no banco, não será inserida.");
            return;
        }

        String sql = """
            INSERT INTO caronas (nome_motorista, telefone_motorista, origem, destino, horario)
            VALUES (?, ?, ?, ?, ?);
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Usuario motorista = carona.getMotorista();

            pstmt.setString(1, motorista.getNome());
            pstmt.setString(2, motorista.getTelefone());
            pstmt.setString(3, carona.getOrigem());
            pstmt.setString(4, carona.getDestino());
            pstmt.setString(5, carona.getHorario());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir carona, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    carona.setId(idGerado);
                } else {
                    throw new SQLException("Falha ao obter ID da carona.");
                }
            }

            System.out.println("Carona cadastrada com sucesso! ID: " + carona.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar carona: " + e.getMessage());
        }
    }

    public List<Carona> listarCaronas() {
        List<Carona> caronas = new ArrayList<>();

        String sql = "SELECT id, nome_motorista, telefone_motorista, origem, destino, horario FROM caronas";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeMotorista = rs.getString("nome_motorista");
                String telefoneMotorista = rs.getString("telefone_motorista");
                String origem = rs.getString("origem");
                String destino = rs.getString("destino");
                String horario = rs.getString("horario");

                Usuario motorista = new Usuario(nomeMotorista, telefoneMotorista);
                Carona carona = new Carona(id, motorista, origem, destino, horario);

                caronas.add(carona);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar caronas: " + e.getMessage());
        }

        return caronas;
    }

    public List<Carona> buscarPorOrigem(String origemBusca) {
        List<Carona> caronasEncontradas = new ArrayList<>();

        String sql = "SELECT * FROM caronas WHERE origem = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, origemBusca);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario motorista = new Usuario(
                            rs.getString("nome_motorista"),
                            rs.getString("telefone_motorista")
                    );

                    Carona carona = new Carona(
                            rs.getInt("id"),
                            motorista,
                            rs.getString("origem"),
                            rs.getString("destino"),
                            rs.getString("horario")
                    );

                    caronasEncontradas.add(carona);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caronas: " + e.getMessage());
        }

        return caronasEncontradas;
    }

    public List<Carona> buscarPorDestino(String destinoBusca) {
        List<Carona> caronasEncontradas = new ArrayList<>();

        String sql = "SELECT * FROM caronas WHERE destino = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, destinoBusca);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario motorista = new Usuario(
                            rs.getString("nome_motorista"),
                            rs.getString("telefone_motorista")
                    );

                    Carona carona = new Carona(
                            rs.getInt("id"),
                            motorista,
                            rs.getString("origem"),
                            rs.getString("destino"),
                            rs.getString("horario")
                    );

                    caronasEncontradas.add(carona);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caronas: " + e.getMessage());
        }

        return caronasEncontradas;
    }

    public void removerDuplicatas() {
        String sql = """
            DELETE FROM caronas
            WHERE id NOT IN (
                SELECT MIN(id)
                FROM caronas
                GROUP BY nome_motorista, telefone_motorista, origem, destino, horario
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Duplicatas removidas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao remover duplicatas: " + e.getMessage());
        }
    }
}
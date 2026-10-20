package com.natan.barbeariabd.dao;

import com.natan.barbeariabd.database.ConnectionFactory;
import com.natan.barbeariabd.model.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public void inserir(Servico servico) throws SQLException {

        String sql =
                "INSERT INTO servico (nome, descricao, duracao, preco) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, servico.getNome());
            statement.setString(2, servico.getDescricao());
            statement.setInt(3, servico.getDuracao());
            statement.setBigDecimal(4, servico.getPreco());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    servico.setIdServico(keys.getInt(1));
                }
            }
        }
    }

    public void atualizar(Servico servico) throws SQLException {

        String sql =
                "UPDATE servico SET nome = ?, descricao = ?, duracao = ?, preco = ? WHERE id_servico = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, servico.getNome());
            statement.setString(2, servico.getDescricao());
            statement.setInt(3, servico.getDuracao());
            statement.setBigDecimal(4, servico.getPreco());
            statement.setInt(5, servico.getIdServico());

            statement.executeUpdate();
        }
    }

    public void excluir(int idServico) throws SQLException {

        String sql =
                "DELETE FROM servico WHERE id_servico = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, idServico);

            statement.executeUpdate();
        }
    }

    public List<Servico> listar() throws SQLException {

        String sql =
                "SELECT id_servico, nome, descricao, duracao, preco FROM servico ORDER BY id_servico";

        List<Servico> servicos = new ArrayList<>();

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Servico servico = new Servico(
                        resultSet.getInt("id_servico"),
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getInt("duracao"),
                        resultSet.getBigDecimal("preco")
                );

                servicos.add(servico);
            }
        }

        return servicos;
    }
}
package com.natan.barbeariabd.dao;

import com.natan.barbeariabd.database.ConnectionFactory;
import com.natan.barbeariabd.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente) throws SQLException {

        String sql =
                "INSERT INTO cliente (cpf, nome, rua, numero) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, cliente.getCpf());
            statement.setString(2, cliente.getNome());
            statement.setString(3, cliente.getRua());
            statement.setString(4, cliente.getNumero());

            statement.executeUpdate();
        }
    }

    public void atualizar(Cliente cliente) throws SQLException {

        String sql =
                "UPDATE cliente SET nome = ?, rua = ?, numero = ? WHERE cpf = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getRua());
            statement.setString(3, cliente.getNumero());
            statement.setString(4, cliente.getCpf());

            statement.executeUpdate();
        }
    }

    public void excluir(String cpf) throws SQLException {

        String sql =
                "DELETE FROM cliente WHERE cpf = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, cpf);

            statement.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {

        String sql =
                "SELECT cpf, nome, rua, numero FROM cliente ORDER BY nome";

        List<Cliente> clientes = new ArrayList<>();

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Cliente cliente = new Cliente(
                        resultSet.getString("cpf"),
                        resultSet.getString("nome"),
                        resultSet.getString("rua"),
                        resultSet.getString("numero")
                );

                clientes.add(cliente);
            }
        }

        return clientes;
    }
}
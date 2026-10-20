package com.natan.barbeariabd.dao;

import com.natan.barbeariabd.database.ConnectionFactory;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultaDAO {

    public DefaultTableModel clientesAgendamentos()
            throws SQLException {

        String sql = """
                SELECT
                    c.nome AS cliente,
                    a.id_agendamento,
                    a.status,
                    p.nome AS barbeiro
                FROM cliente c
                JOIN agendamento a
                    ON a.cpf_cliente = c.cpf
                JOIN barbeiro b
                    ON b.cpf_profissional = a.cpf_barbeiro
                JOIN profissional p
                    ON p.cpf = b.cpf_profissional
                ORDER BY a.id_agendamento
                """;

        return executarTabela(sql);
    }

    public DefaultTableModel agendamentosPorBarbeiro()
            throws SQLException {

        String sql = """
                SELECT
                    p.nome AS barbeiro,
                    COUNT(a.id_agendamento) AS total_agendamentos
                FROM barbeiro b
                JOIN profissional p
                    ON p.cpf = b.cpf_profissional
                LEFT JOIN agendamento a
                    ON a.cpf_barbeiro = b.cpf_profissional
                GROUP BY p.cpf, p.nome
                ORDER BY total_agendamentos DESC, p.nome
                """;

        return executarTabela(sql);
    }

    public DefaultTableModel valorTotalAtendimentos()
            throws SQLException {

        String sql = """
                SELECT
                    at.id_atendimento,
                    c.nome AS cliente,
                    COALESCE(
                        SUM(ia.preco_praticado),
                        0
                    ) AS valor_total
                FROM atendimento at
                LEFT JOIN agendamento ag
                    ON ag.id_agendamento = at.id_agendamento
                LEFT JOIN cliente c
                    ON c.cpf = ag.cpf_cliente
                LEFT JOIN item_atendimento ia
                    ON ia.id_atendimento = at.id_atendimento
                GROUP BY
                    at.id_atendimento,
                    c.nome
                ORDER BY at.id_atendimento
                """;

        return executarTabela(sql);
    }

    public DefaultTableModel servicosMaisRealizados()
            throws SQLException {

        String sql = """
                SELECT
                    s.nome AS servico,
                    COUNT(ia.id_servico) AS quantidade_realizada,
                    COALESCE(ROUND(AVG(ia.preco_praticado), 2), 0) AS preco_medio,
                    COALESCE(ROUND(SUM(ia.preco_praticado), 2), 0) AS faturamento_total
                FROM servico s
                LEFT JOIN item_atendimento ia
                    ON ia.id_servico = s.id_servico
                GROUP BY
                    s.id_servico,
                    s.nome
                ORDER BY quantidade_realizada DESC, faturamento_total DESC, s.nome
                """;

        return executarTabela(sql);
    }

    public Map<String, Double> dadosAgendamentosPorBarbeiro()
            throws SQLException {

        String sql = """
                SELECT
                    p.nome AS barbeiro,
                    COUNT(a.id_agendamento) AS total
                FROM barbeiro b
                JOIN profissional p
                    ON p.cpf = b.cpf_profissional
                LEFT JOIN agendamento a
                    ON a.cpf_barbeiro = b.cpf_profissional
                GROUP BY
                    p.cpf,
                    p.nome
                ORDER BY total DESC, p.nome
                LIMIT 10
                """;

        return executarGrafico(sql);
    }

    public Map<String, Double> dadosServicosMaisRealizados()
            throws SQLException {

        String sql = """
                SELECT
                    s.nome AS servico,
                    COUNT(ia.id_servico) AS total
                FROM servico s
                LEFT JOIN item_atendimento ia
                    ON ia.id_servico = s.id_servico
                GROUP BY
                    s.id_servico,
                    s.nome
                ORDER BY total DESC, s.nome
                LIMIT 10
                """;

        return executarGrafico(sql);
    }

    private DefaultTableModel executarTabela(String sql)
            throws SQLException {

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            ResultSetMetaData metaData =
                    resultSet.getMetaData();

            int quantidadeColunas =
                    metaData.getColumnCount();

            DefaultTableModel model =
                    new DefaultTableModel() {

                        @Override
                        public boolean isCellEditable(
                                int row,
                                int column
                        ) {
                            return false;
                        }
                    };

            for (
                    int i = 1;
                    i <= quantidadeColunas;
                    i++
            ) {

                model.addColumn(
                        metaData.getColumnLabel(i)
                );
            }

            while (resultSet.next()) {

                Object[] linha =
                        new Object[quantidadeColunas];

                for (
                        int i = 1;
                        i <= quantidadeColunas;
                        i++
                ) {

                    linha[i - 1] =
                            resultSet.getObject(i);
                }

                model.addRow(linha);
            }

            return model;
        }
    }

    private Map<String, Double> executarGrafico(
            String sql
    ) throws SQLException {

        Map<String, Double> dados =
                new LinkedHashMap<>();

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                dados.put(
                        resultSet.getString(1),
                        resultSet.getDouble(2)
                );
            }
        }

        return dados;
    }
}
package com.natan.barbeariabd.view;

import com.natan.barbeariabd.dao.ConsultaDAO;

import javax.swing.*;
import java.awt.*;

public class ConsultasPanel extends JPanel {

    private final ConsultaDAO dao =
            new ConsultaDAO();

    private final JComboBox<String> consultas =
            new JComboBox<>(
                    new String[]{
                            "Clientes e agendamentos",
                            "Agendamentos por barbeiro",
                            "Valor total dos atendimentos",
                            "Serviços mais realizados"
                    }
            );

    private final JTable tabela =
            new JTable();

    public ConsultasPanel() {

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JButton executar =
                new JButton(
                        "Executar consulta"
                );

        JPanel topo =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        topo.add(consultas);
        topo.add(executar);

        add(
                topo,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(tabela),
                BorderLayout.CENTER
        );

        executar.addActionListener(
                e -> executar()
        );
    }

    private void executar() {

        try {

            switch (
                    consultas.getSelectedIndex()
            ) {

                case 0 ->
                        tabela.setModel(
                                dao.clientesAgendamentos()
                        );

                case 1 ->
                        tabela.setModel(
                                dao.agendamentosPorBarbeiro()
                        );

                case 2 ->
                        tabela.setModel(
                                dao.valorTotalAtendimentos()
                        );

                case 3 ->
                        tabela.setModel(
                                dao.servicosMaisRealizados()
                        );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
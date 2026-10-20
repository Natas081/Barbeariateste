package com.natan.barbeariabd.view;

import com.natan.barbeariabd.dao.ConsultaDAO;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final ConsultaDAO dao =
            new ConsultaDAO();

    private final BarChartPanel graficoBarbeiros =
            new BarChartPanel(
                    "Agendamentos por barbeiro"
            );

    private final BarChartPanel graficoServicos =
            new BarChartPanel(
                    "Serviços mais realizados"
            );

    public DashboardPanel() {

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

        JButton atualizar =
                new JButton(
                        "Atualizar dashboard"
                );

        JPanel graficos =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );

        graficos.add(
                graficoBarbeiros
        );

        graficos.add(
                graficoServicos
        );

        add(
                atualizar,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(graficos),
                BorderLayout.CENTER
        );

        atualizar.addActionListener(
                e -> carregar()
        );

        carregar();
    }

    private void carregar() {

        try {

            graficoBarbeiros.setDados(
                    dao.dadosAgendamentosPorBarbeiro()
            );

            graficoServicos.setDados(
                    dao.dadosServicosMaisRealizados()
            );

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
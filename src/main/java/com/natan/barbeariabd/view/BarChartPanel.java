package com.natan.barbeariabd.view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BarChartPanel extends JPanel {

    private final String titulo;

    private Map<String, Double> dados =
            new LinkedHashMap<>();

    public BarChartPanel(
            String titulo
    ) {

        this.titulo = titulo;

        setPreferredSize(
                new Dimension(
                        800,
                        300
                )
        );

        setBackground(Color.WHITE);
        setFont(new Font("SansSerif", Font.PLAIN, 13));
        setBorder(BorderFactory.createLineBorder(new Color(224, 220, 211)));
    }

    public void setDados(
            Map<String, Double> dados
    ) {

        this.dados = dados;

        repaint();
    }

    @Override
    protected void paintComponent(
            Graphics graphics
    ) {

        super.paintComponent(graphics);

        Graphics2D g =
                (Graphics2D) graphics;

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(
                StudioTheme.INK
        );

        g.drawString(
                titulo,
                20,
                25
        );

        if (
                dados == null
                        || dados.isEmpty()
        ) {

            g.drawString(
                    "Sem dados para exibir",
                    20,
                    55
            );

            return;
        }

        double maximo =
                dados
                        .values()
                        .stream()
                        .mapToDouble(
                                Double::doubleValue
                        )
                        .max()
                        .orElse(1);

        if (maximo == 0) {
            maximo = 1;
        }

        int margemEsquerda = 180;

        int larguraMaxima =
                Math.max(
                        1,
                        getWidth()
                                - margemEsquerda
                                - 70
                );

        int alturaLinha =
                Math.max(
                        22,
                        (getHeight() - 55)
                                / dados.size()
                );

        int alturaBarra =
                Math.max(
                        10,
                        alturaLinha - 8
                );

        FontMetrics metrics =
                g.getFontMetrics();

        int indice = 0;

        for (
                Map.Entry<String, Double> entry
                : dados.entrySet()
        ) {

            int y =
                    45
                            + indice
                            * alturaLinha;

            String nome =
                    entry.getKey();

            if (
                    nome.length() > 22
            ) {

                nome =
                        nome.substring(
                                0,
                                19
                        )
                                + "...";
            }

            g.setColor(
                    StudioTheme.INK
            );

            g.drawString(
                    nome,
                    margemEsquerda
                            - metrics.stringWidth(nome)
                            - 10,
                    y
                            + alturaBarra
                            - 2
            );

            int larguraBarra =
                    (int) Math.round(
                            (
                                    entry.getValue()
                                            / maximo
                            )
                                    * larguraMaxima
                    );

            g.setColor(
                    new Color(
                            166,
                            103,
                            63
                    )
            );

            g.fillRoundRect(
                    margemEsquerda,
                    y,
                    larguraBarra,
                    alturaBarra,
                    8,
                    8
            );

            g.setColor(
                    StudioTheme.INK
            );

            g.drawString(
                    String.format(
                            "%.0f",
                            entry.getValue()
                    ),
                    margemEsquerda
                            + larguraBarra
                            + 8,
                    y
                            + alturaBarra
                            - 2
            );

            indice++;
        }
    }
}
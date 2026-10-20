package com.natan.barbeariabd.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/** Landing screen intentionally uses no database calls or fictional business metrics. */
public class WelcomePanel extends JPanel {
    public WelcomePanel(Consumer<String> navigate) {
        setLayout(new BorderLayout(0, 24));
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int x = getWidth()-180;
                g.setColor(new Color(47, 53, 47));
                g.drawOval(x-100, -80, 380, 380);
                g.drawOval(x-70, -50, 320, 320);
                g.setColor(new Color(192, 158, 119));
                g.fillRoundRect(x, 66, 60, 166, 26, 26);
                g.setClip(x+6, 74, 48, 150);
                for(int y=-40; y<300; y+=48) {
                    g.setColor(new Color(242, 231, 207));
                    g.fillPolygon(new int[]{x+6,x+54,x+54,x+6}, new int[]{y+40,y+8,y+30,y+62},4);
                }
                g.setClip(null);
                g.setColor(new Color(192, 158, 119));
                g.fillRoundRect(x-9, 60, 78, 12, 8, 8);
                g.fillRoundRect(x-9, 226, 78, 12, 8, 8);
                g.dispose();
            }
        };
        hero.setBackground(StudioTheme.INK);
        hero.setBorder(BorderFactory.createEmptyBorder(34, 30, 34, 180));
        hero.setPreferredSize(new Dimension(600, 300));
        JPanel copy = new JPanel(); copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(StudioTheme.label("TRADIÇÃO NO CORTE. EVOLUÇÃO NA GESTÃO.", 10, new Color(192,158,119)));
        copy.add(Box.createVerticalStrut(20));
        JLabel headline = StudioTheme.label("<html>Seu talento merece<br>uma gestão à altura.</html>", 34, StudioTheme.PAPER);
        headline.setFont(new Font("Serif", Font.BOLD, 36)); copy.add(headline);
        copy.add(Box.createVerticalStrut(15));
        copy.add(StudioTheme.label("<html>Mais cuidado com o negócio.<br>Mais tempo para fazer o seu melhor.</html>", 14, new Color(184,188,175)));
        hero.add(copy, BorderLayout.CENTER); add(hero, BorderLayout.NORTH);
        JPanel bottom = new JPanel(new BorderLayout(0, 18));
        JPanel section = new JPanel(new BorderLayout());
        section.add(StudioTheme.label("Tudo no seu lugar", 22, StudioTheme.INK), BorderLayout.WEST);
        section.add(StudioTheme.label("ESCOLHA POR ONDE COMEÇAR", 10, StudioTheme.MUTED), BorderLayout.EAST);
        bottom.add(section, BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1,3,16,0));
        cards.add(card("01 / RELACIONAMENTO", "Clientes", "Cada cliente, uma história.", "Gerencie cadastros e mantenha<br>os dados sempre por perto.", "Abrir clientes  →", () -> navigate.accept("Seus clientes")));
        cards.add(card("02 / SEU OFÍCIO", "Serviços", "Valorize cada detalhe.", "Organize cortes, cuidados,<br>duração e preços.", "Ver serviços  →", () -> navigate.accept("Catálogo de serviços")));
        cards.add(card("03 / RESULTADOS", "Dashboard", "Olhe para o próximo nível.", "Acompanhe os serviços e<br>agendamentos da equipe.", "Ver desempenho  →", () -> navigate.accept("Desempenho do negócio")));
        bottom.add(cards, BorderLayout.CENTER);
        bottom.add(StudioTheme.label("✂   ORGANIZAÇÃO, ESTILO E PERSONALIDADE. DO SEU JEITO.", 10, StudioTheme.MUTED), BorderLayout.SOUTH);
        add(bottom, BorderLayout.CENTER);
    }
    private JPanel card(String overline, String title, String tagline, String text, String action, Runnable onClick) {
        JPanel p = new JPanel(new BorderLayout(0,16));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(224,220,211)), BorderFactory.createEmptyBorder(22,18,20,18)));
        p.add(StudioTheme.label(overline,10,StudioTheme.COPPER), BorderLayout.NORTH);
        JLabel words = StudioTheme.label("<html><div style='font-size:22px'>"+title+"</div><br>"
                +"<span style='font-size:10px'>"+tagline+"</span><br><br>"
                +"<span style='font-size:10px;color:#71746c'>"+text+"</span></html>", 12, StudioTheme.INK);
        words.setVerticalAlignment(SwingConstants.TOP);
        p.add(words,BorderLayout.CENTER);
        JButton b = new JButton(action); b.addActionListener(e -> onClick.run()); p.add(b,BorderLayout.SOUTH);
        return p;
    }
}

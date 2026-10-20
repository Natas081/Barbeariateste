package com.natan.barbeariabd.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
    private final JPanel content = new JPanel(new BorderLayout());
    private final JLabel title = StudioTheme.label("Visão geral", 28, StudioTheme.INK);
    private JButton active;

    public MainFrame() {
        StudioTheme.install();
        setTitle("Barbearia • Studio de gestão");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 830);
        setMinimumSize(new Dimension(1140, 820));
        setLocationRelativeTo(null);
        JPanel shell = new JPanel(new BorderLayout());
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setBackground(StudioTheme.INK);
        sidebar.setBorder(BorderFactory.createEmptyBorder(34, 22, 26, 22));
        JPanel brand = new JPanel(new GridLayout(3, 1, 0, 3));
        brand.setOpaque(false);
        brand.add(StudioTheme.label("✂  BARBEARIA", 22, StudioTheme.PAPER));
        brand.add(StudioTheme.label("STUDIO DE GESTÃO", 10, new Color(192, 158, 119)));
        brand.add(StudioTheme.label("______________________", 12, new Color(66, 69, 64)));
        sidebar.add(brand, BorderLayout.NORTH);
        JPanel links = new JPanel();
        links.setOpaque(false);
        links.setLayout(new BoxLayout(links, BoxLayout.Y_AXIS));
        links.setBorder(BorderFactory.createEmptyBorder(42, 0, 0, 0));
        links.add(StudioTheme.label("  SEU ESPAÇO", 10, new Color(145, 151, 141)));
        links.add(Box.createVerticalStrut(18));
        nav(links, "01   Visão geral", "Visão geral", () -> new WelcomePanel(this::open));
        nav(links, "02   Clientes", "Seus clientes", ClientePanel::new);
        nav(links, "03   Serviços", "Catálogo de serviços", ServicoPanel::new);
        nav(links, "04   Consultas", "Relatórios e consultas", ConsultasPanel::new);
        nav(links, "05   Dashboard", "Desempenho do negócio", DashboardPanel::new);
        sidebar.add(links, BorderLayout.CENTER);
        JLabel footer = StudioTheme.label("<html>FEITO PARA O SEU OFÍCIO<br><br><font color='#ddd4c5'>Cuidado em cada detalhe.</font></html>", 10, new Color(145, 151, 141));
        sidebar.add(footer, BorderLayout.SOUTH);
        JPanel main = new JPanel(new BorderLayout(0, 24));
        main.setBorder(BorderFactory.createEmptyBorder(30, 32, 28, 32));
        JPanel heading = new JPanel(new BorderLayout());
        JPanel words = new JPanel(new GridLayout(2, 1, 0, 6));
        words.add(StudioTheme.label("BARBEARIA  /  PAINEL DE GESTÃO", 10, StudioTheme.MUTED));
        words.add(title);
        heading.add(words, BorderLayout.WEST);
        heading.add(StudioTheme.label(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("pt-BR"))), 12, StudioTheme.MUTED), BorderLayout.EAST);
        main.add(heading, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        shell.add(sidebar, BorderLayout.WEST);
        shell.add(main, BorderLayout.CENTER);
        setContentPane(shell);
        open("Visão geral");
    }
    private void nav(JPanel panel, String text, String page, Supplier<JPanel> factory) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 48));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 8));
        button.setFocusPainted(false);
        button.setForeground(StudioTheme.PAPER);
        button.setBackground(StudioTheme.INK);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("page", page);
        button.putClientProperty("factory", factory);
        button.addActionListener(e -> {
            if (active != null) active.setBackground(StudioTheme.INK);
            active = button;
            button.setBackground(new Color(64, 67, 58));
            title.setText(page);
            content.removeAll();
            JPanel view = factory.get();
            StudioTheme.style(view);
            content.add(view);
            content.revalidate(); content.repaint();
        });
        panel.add(button);
        panel.add(Box.createVerticalStrut(6));
    }
    private void open(String page) {
        JPanel sidebar = (JPanel)((JPanel)getContentPane()).getComponent(0);
        JPanel links = (JPanel)((BorderLayout)sidebar.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        for (Component c : links.getComponents())
            if (c instanceof JButton b && page.equals(b.getClientProperty("page"))) { b.doClick(); return; }
    }
}

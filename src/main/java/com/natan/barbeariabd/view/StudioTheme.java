package com.natan.barbeariabd.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/** Shared visual language, using only the JDK Swing toolkit. */
public final class StudioTheme {
    public static final Color INK = new Color(25, 29, 28);
    public static final Color PAPER = new Color(245, 242, 235);
    public static final Color COPPER = new Color(166, 103, 63);
    public static final Color MUTED = new Color(113, 116, 108);
    private StudioTheme() {}
    public static void install() {
        UIManager.put("Panel.background", PAPER);
        UIManager.put("Label.foreground", INK);
        UIManager.put("OptionPane.background", PAPER);
        UIManager.put("OptionPane.messageForeground", INK);
        UIManager.put("TextField.caretForeground", COPPER);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", INK);
        for (String key : new String[]{"Label.font", "Button.font", "TextField.font", "Table.font", "ComboBox.font", "OptionPane.messageFont"})
            UIManager.put(key, new Font("SansSerif", Font.PLAIN, 14));
    }
    public static JLabel label(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }
    public static void style(Component component) {
        if (component instanceof JTextField field) {
            field.setBackground(Color.WHITE);
            field.setForeground(INK);
            field.setPreferredSize(new Dimension(180, 42));
            field.setBorder(new CompoundBorder(new LineBorder(new Color(219, 215, 206)), new EmptyBorder(9, 12, 9, 12)));
        } else if (component instanceof JButton button) {
            boolean primary = button.getText().startsWith("Inserir") || button.getText().startsWith("Executar");
            button.setBackground(primary ? INK : Color.WHITE);
            button.setForeground(primary ? Color.WHITE : INK);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(new CompoundBorder(new LineBorder(new Color(219, 215, 206)), new EmptyBorder(11, 20, 11, 20)));
            button.setOpaque(true);
        } else if (component instanceof JTable table) {
            table.setRowHeight(46);
            table.setShowGrid(false);
            table.setIntercellSpacing(new Dimension(0, 0));
            table.setSelectionBackground(new Color(232, 217, 199));
            table.setSelectionForeground(INK);
            table.setFillsViewportHeight(true);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(INK);
            table.getTableHeader().setForeground(PAPER);
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
            table.getTableHeader().setPreferredSize(new Dimension(0, 44));
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(JTable t, Object value, boolean selected, boolean focus, int row, int col) {
                    super.getTableCellRendererComponent(t, value, selected, focus, row, col);
                    setBorder(new EmptyBorder(0, 14, 0, 14));
                    if (!selected) { setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 247, 243)); setForeground(INK); }
                    return this;
                }
            });
        } else if (component instanceof JScrollPane scroll) {
            scroll.setBorder(new LineBorder(new Color(224, 220, 211)));
            scroll.getViewport().setBackground(Color.WHITE);
        }
        if (component instanceof Container container)
            for (Component child : container.getComponents()) style(child);
    }
}

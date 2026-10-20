package com.natan.barbeariabd;

import com.natan.barbeariabd.view.MainFrame;

import javax.swing.SwingUtilities;

public class BarbeariaBdApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
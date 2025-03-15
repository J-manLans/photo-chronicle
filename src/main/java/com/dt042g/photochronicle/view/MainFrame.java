package com.dt042g.photochronicle.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * This class represents the main application window.
 *
 * <p>It extends {@link JFrame} and is responsible for setting up the main UI layout.</p>
 * @author Joel Lansgren
 */
public final class MainFrame extends JFrame {

    /**
     * Constructs the MainFrame.
     *
     * <p>It sets up the MainFrame with a {@link BorderLayout}, a fixed preferred size,
     * and a non-resizable window, a title-bar title with the app name, adds all
     * its JPanels and make sure the frame is displayed in the centre of the
     * screen on startup.</p>
     * @param topPanel the panel to be placed at the top (NORTH) of the frame
     * @param middlePanel the panel to be placed at the center of the frame
     * @param bottomPanel the panel to be placed at the bottom (SOUTH) of the frame
     */
    public MainFrame(final JPanel topPanel, final JPanel middlePanel, final JPanel bottomPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(AppConfig.APP_DIMENSION);
        setResizable(false);
        setTitle(AppConfig.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }
}

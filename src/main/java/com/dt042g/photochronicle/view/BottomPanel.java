package com.dt042g.photochronicle.view;

import com.dt042g.photochronicle.support.AppConfig;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * Class representing the bottom panel of the main window, and extends {@link JPanel}.
 * @author Daniel Berg
 */
public final class BottomPanel extends JPanel {
    private final JButton infoButton = new JButton("Info");

    /**
     * Constructor of the class.
     * Sets up a {@link FlowLayout} for the {@link JPanel} with right alignment,
     * and desired horizontal and vertical padding.
     * A {@link JButton} is also added to the panel.
     */
    public BottomPanel() {
        final FlowLayout layout = new FlowLayout();
        setLayout(layout);
        layout.setAlignment(FlowLayout.RIGHT);
        layout.setHgap(AppConfig.FLOW_GAP);
        layout.setVgap(AppConfig.FLOW_GAP);
        add(infoButton);
    }

    /**
     * Adds a listener that opens the dialog to the infoButton.
     * @param listener the listener that will be attached to the button.
     */
    public void addInfoButtonListener(final ActionListener listener) {
        infoButton.addActionListener(listener);
    }
}

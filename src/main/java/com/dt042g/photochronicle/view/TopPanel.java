package com.dt042g.photochronicle.view;

import com.dt042g.photochronicle.support.AppConfig;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Class representing the top panel of the main window, and extends {@link JPanel}.
 * @author Daniel Berg
 */
public final class TopPanel extends JPanel {
    private final JLabel title = new JLabel();

    /**
     * Constructor of the class.
     * Sets up a {@link FlowLayout} for the {@link JPanel} with left alignment,
     * and desired horizontal and vertical padding.
     * A {@link JLabel} is also added to the panel with the title of the application.
     */
    public TopPanel() {
        final FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        setLayout(flowLayout);
        flowLayout.setHgap(AppConfig.FLOW_GAP);
        flowLayout.setVgap(AppConfig.FLOW_GAP);

        title.setText(AppConfig.APP_NAME);
        title.setFont(new Font("Monospace", Font.ITALIC, AppConfig.TEXT_SIZE_TITLE));
        add(title);
    }
}

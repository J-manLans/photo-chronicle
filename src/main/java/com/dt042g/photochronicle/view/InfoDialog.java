package com.dt042g.photochronicle.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.dt042g.photochronicle.support.AppConfig;

public final class InfoDialog extends JDialog {
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel infoDialogPanel = new JPanel(new GridBagLayout());
    private final JLabel infoMessage = new JLabel(AppConfig.HTML_INFO_LABEL);
    private final JButton infoCloseBtn = new JButton("Close");

    /**
     * Constructs and configures an information dialog with static content.
     * The dialog is modal, ensuring it blocks interaction with the main window while open.
     * It is displayed at the center of the application and features a close button.
     *
     * <p>Note: This constructor should be called after the MainFrame is initialized so it
     * can be set as its owner ensuring it will always open in the center of the app.</p>
     *
     * @param mainFrame the main application window, used as the dialog's owner
     */
    public InfoDialog(final JFrame mainFrame) {
        super(mainFrame, true);

        // Configure dialog appearance
        setSize(AppConfig.DIALOG_DIMENSION);
        setUndecorated(true);
        infoDialogPanel.setBorder(new LineBorder(Color.DARK_GRAY));

        // Configure layout and add components
        gbc.gridy = 0;
        gbc.insets = new Insets(AppConfig.FLOW_GAP, AppConfig.FLOW_GAP, AppConfig.FLOW_GAP, AppConfig.FLOW_GAP);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoDialogPanel.add(infoMessage, gbc);

        gbc.gridy++;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, AppConfig.FLOW_GAP, AppConfig.FLOW_GAP);
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        infoDialogPanel.add(infoCloseBtn, gbc);

        add(infoDialogPanel);
    }
}


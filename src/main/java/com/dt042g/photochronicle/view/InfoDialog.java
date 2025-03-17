package com.dt042g.photochronicle.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

    public InfoDialog(JFrame mainFrame) {
        super(mainFrame, true);

        // Configure dialog appearance
        setSize(AppConfig.DIALOG_DIMENSION);
        setUndecorated(true);
        infoDialogPanel.setBorder(new LineBorder(Color.DARK_GRAY));
    }
}


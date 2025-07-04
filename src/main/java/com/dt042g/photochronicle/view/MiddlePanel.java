package com.dt042g.photochronicle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Class representing the middle panel of the MainFrame, and extends {@link JPanel}.
 *
 * <p>It consist of a wrapper panel for a JLabel that displays selected folders,
 * and JButton that has the ability to clear the content of the label, another
 * JButton for selecting and sorting images in a folder and a JFileChooser that
 * lets that happen.</p>
 * @author Joel Lansgren, Daniel Berg
 */
public final class MiddlePanel extends JPanel {
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel labelAndClearBtnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    private final JLabel pathLabel = new JLabel(AppConfig.CHOOSE_FOLDER);
    private final JButton clearBtn = new JButton("Clear");
    private final JButton chooseFolderBtn = new JButton("Choose Folder");
    private final JFileChooser fileChooser = new JFileChooser();

    /**
     * Constructor of the class.
     * Sets up a {@link GridBagLayout} for the {@link JPanel},
     * Then it styles and set up the components that will be added to
     * the JPanel.
     */
    public MiddlePanel() {
        setLayout(new GridBagLayout());

        // Sets up the label and button wrapper panel.
        labelAndClearBtnWrapper.setBackground(AppConfig.CLR_LIGHT);

        // Sets up the pathLabel
        pathLabel.setBorder(new EmptyBorder(
            0,
            AppConfig.PADDING_FOR_PATH_LABEL,
            0,
            AppConfig.PADDING_FOR_PATH_LABEL
        ));
        pathLabel.setFont(new Font("Monospace", Font.ITALIC, AppConfig.TEXT_SIZE_NORMAL));
        pathLabel.setForeground(AppConfig.CLR_PATH_LABEL);
        pathLabel.setPreferredSize(
            new Dimension(AppConfig.FOLDER_PATH_WIDTH, (int) chooseFolderBtn.getPreferredSize().getHeight())
        );

        // Sets up the fileChooser
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(AppConfig.CHOOSE_FOLDER);

        // Adds components to panels
        labelAndClearBtnWrapper.add(pathLabel);
        labelAndClearBtnWrapper.add(clearBtn);
        add(labelAndClearBtnWrapper, gbc);
        gbc.insets = new Insets(0, AppConfig.FLOW_GAP, 0, 0);
        add(chooseFolderBtn, gbc);
    }

    /**
     * Used to show the folder selection dialog.
     * @param sortFolder a callback to the controller that starts the sorting function in the model.
     */
    public void showFolderSelectionDialog(final Consumer<String> sortFolder) {

        final int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            resetPathColor();
            pathLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
            sortFolder.accept(pathLabel.getText());
        }
    }

    /*=====================
    * Listener Methods
    =====================*/

    /**
     * Adds a listener to the add/sort button.
     * @param listener the listener to be attached to the button.
     */
    public void addListenerToFolderButton(final ActionListener listener) {
        chooseFolderBtn.addActionListener(listener);
    }

    /**
     * Adds a listener to the clear button.
     * @param listener the listener to be attached to the button.
     */
    public void addListenerToClearButton(final ActionListener listener) {
        clearBtn.addActionListener(listener);
    }

    /*=================
    * Setters
    =================*/

    /**
     * Used to clear the path label and reset the add/sort button.
     */
    public void clearSelection() {
        resetPathColor();
        pathLabel.setText(AppConfig.CHOOSE_FOLDER);
    }

    /**
     * Sets the color of the pathLabel to red when the chosen folder is not accessible.
     */
    public void setErrorColorPath() {
        pathLabel.setForeground(Color.RED);
    }

    /**
     * Resets the color of the pathLabel.
     */
    public void resetPathColor() {
        pathLabel.setForeground(AppConfig.CLR_PATH_LABEL);
    }
}

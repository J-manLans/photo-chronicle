package com.dt042g.photochronicle.controller;

import com.dt042g.photochronicle.view.BottomPanel;
import com.dt042g.photochronicle.view.InfoDialog;
import com.dt042g.photochronicle.view.MainFrame;
import com.dt042g.photochronicle.view.MiddlePanel;
import com.dt042g.photochronicle.view.TopPanel;

/**
 * @author Joel Lansgren
 */
public final class ChronicleController {
    private final TopPanel topPanel;
    private final MiddlePanel middlePanel;
    private final BottomPanel bottomPanel;
    private final MainFrame mainFrame;
    private final InfoDialog infoDialog;

    /**
     * Constructs the controller and instantiates all views and models that's part of the MVC pattern.
     */
    public ChronicleController() {
        topPanel = new TopPanel();
        middlePanel = new MiddlePanel();
        bottomPanel = new BottomPanel();
        mainFrame = new MainFrame(topPanel, middlePanel, bottomPanel);
        infoDialog = new InfoDialog(mainFrame);
    }

    /**
     * Initializes different parts off the application,
     * then sets the {@link MainFrame} to visible.
     */
    public void initialize() {
        initializeListeners();
        mainFrame.setVisible(true);
    }

    /**
     * Initialize listeners to the application components.
     */
    public void initializeListeners() {
        bottomPanel.addInfoButtonListener(e -> {
            infoDialog.setLocationRelativeTo(infoDialog.getOwner());
            infoDialog.setVisible(true);
        });
        infoDialog.addInfoCloseBtnListener(e -> infoDialog.setVisible(false));
    }

    /*=====================
    * Getters
    =====================*/

    /**
     * Returns the Mainframe so test classes can set up test environments.
     * @return The MainFrame.
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Returns the TopPanel so test classes can set up test environments.
     * @return The TopPanel.
     */
    public TopPanel getTopPanel() {
        return topPanel;
    }

    /**
     * Returns the MiddlePanel so test classes can set up test environments.
     * @return The MiddlePanel.
     */
    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    /**
     * Returns the BottomPanel so test classes can set up test environments.
     * @return The BottomPanel.
     */
    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    /**
     * Returns the InfoDialog so test classes can set up test environments.
     * @return The InfoDialog.
     */
    public InfoDialog getInfoDialog() {
        return infoDialog;
    }
}

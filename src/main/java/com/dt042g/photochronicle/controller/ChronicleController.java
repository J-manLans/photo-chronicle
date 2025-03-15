package com.dt042g.photochronicle.controller;

import com.dt042g.photochronicle.view.BottomPanel;
import com.dt042g.photochronicle.view.MainFrame;
import com.dt042g.photochronicle.view.MiddlePanel;
import com.dt042g.photochronicle.view.TopPanel;

public class ChronicleController {
    private final TopPanel topPanel;
    private final MiddlePanel middlePanel;
    private final BottomPanel bottomPanel;
    private final MainFrame mainFrame;

    /**
     * Constructs the controller and instantiates all views and models that's part of the MVC pattern.
     * Then sets the {@link MainFrame} to visible.
     */
    public ChronicleController() {
        topPanel = new TopPanel();
        middlePanel = new MiddlePanel();
        bottomPanel = new BottomPanel();
        mainFrame = new MainFrame(topPanel, middlePanel, bottomPanel);

        mainFrame.setVisible(true);
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
}

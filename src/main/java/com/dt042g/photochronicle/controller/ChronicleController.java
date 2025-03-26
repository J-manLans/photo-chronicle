package com.dt042g.photochronicle.controller;

import com.dt042g.photochronicle.model.ChronicleModel;
import com.dt042g.photochronicle.view.BottomPanel;
import com.dt042g.photochronicle.view.InfoDialog;
import com.dt042g.photochronicle.view.MainFrame;
import com.dt042g.photochronicle.view.MiddlePanel;
import com.dt042g.photochronicle.view.TopPanel;

/**
 * The {@code ChronicleController} class is responsible for acting as an intermediary between the model and view of the
 * application. Upon instantiation the class will create the necessary model, as well as all the view classes needed to
 * make up the graphical interface of the application.
 *
 * @author Joel Lansgren, Daniel Berg
 */
public final class ChronicleController {
    private final TopPanel topPanel;
    private final MiddlePanel middlePanel;
    private final BottomPanel bottomPanel;
    private final MainFrame mainFrame;
    private final InfoDialog infoDialog;
    private final ChronicleModel chronicleModel;

    /**
     * Constructs the controller and instantiates all views and models that's part of the MVC pattern.
     */
    public ChronicleController() {
        topPanel = new TopPanel();
        middlePanel = new MiddlePanel();
        bottomPanel = new BottomPanel();
        mainFrame = new MainFrame(topPanel, middlePanel, bottomPanel);
        infoDialog = new InfoDialog(mainFrame);
        chronicleModel = new ChronicleModel();
    }

    /**
     * Initializes different parts off the application,
     * then sets the {@link MainFrame} to visible.
     */
    public void initialize() {
        initializeListeners();
        showMainFrame();
    }

    /*=====================
    * Helper Methods
    =====================*/

    /**
     * Initialize listeners to the application components.
     */
    void initializeListeners() {
        bottomPanel.addInfoButtonListener(e -> infoDialog.showDialog());
        infoDialog.addInfoCloseBtnListener(e -> infoDialog.hideDialog());

        middlePanel.addListenerToFolderButton(event -> middlePanel.showFolderSelectionDialog(this::sortFolder));
        middlePanel.addListenerToClearButton(event -> middlePanel.clearSelection());
    }

    /**
     * Shows the main frame.
     */
    void showMainFrame() {
        mainFrame.setVisible(true);
    }

    /**
     * Sets the path in the model and start its sortFolder method.
     * @param path The path to the user selected folder as a string.
     */
    void sortFolder(final String path) {
        chronicleModel.setPath(path);
        chronicleModel.sortFolder(this::displayError, this::displayInformation);
    }

    private void displayInformation(final String information) {
        infoDialog.setMessage(information);
        infoDialog.showDialog();
    }

    private void displayError(final String errorMessage) {
        middlePanel.setErrorColorPath();
        infoDialog.setMessage(errorMessage);
        infoDialog.showDialog();
    }
}

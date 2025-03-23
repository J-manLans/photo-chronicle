package com.dt042g.photochronicle.support;

import java.awt.Color;
import java.awt.Dimension;



/**
 * A utility class that provides configuration constants for the photo chronicler application.
 *
 * <p>This class is designed to hold static configuration values related to the sorting of image
 * folders by year and month. The main purpose of this class is to centralize and manage
 * constants or configuration values for the application.</p>
 *
 * <p>The {@link AppConfig} class cannot be instantiated, as it is intended purely for static use.</p>
 *
 * @author Joel Lansgren, Daniel Berg
 */
public final class AppConfig {

    private AppConfig() { // Private constructor to prevent instantiation.
        throw new IllegalStateException("Utility class");
    }

    /*=========================
    * View
    =========================*/

    /* ===== Strings ===== */

    /** The name of the application. */
    public static final String APP_NAME = "PhotoChronicle";

    /** The html string for the JLabel that is in the info dialog. */
    public static final String HTML_INFO_LABEL = "<html><h3>Welcome to PhotoChronicle</h3><p>This app sorts folders"
    + " that contain images into sub-folders of year and date using their metadata.</p><p>To do this, just click the"
    + " <i>Add Folder</i> button and choose a folder that contains the images you want to sort. After that you click"
    + " the same button that now says <i>Sort Folder</i>, and the folder will be sorted.</p></html>";

    /** String to be displayed when no folder has been selected. */
    public static final String NO_FOLDER_SELECTED = "Choose image folder";

    /** Label of folder button when its adding folder. */
    public static final String ADD_FOLDER_BUTTON = "Add Folder";

    /** Label of folder button when its sorting folder. */
    public static final String SORT_FOLDER_BUTTON = "Sort Folder";

    /* ===== Numbers ===== */

    /** An integer that defines hgap or vgap  between components in a panel that has FlowLayout. */
    public static final int FLOW_GAP = 15;

    /** An integer that defines the size of the title text. */
    public static final int TEXT_SIZE_TITLE = 28;

    /** An integer that defines the size of normal text. */
    public static final int TEXT_SIZE_NORMAL = 14;

    /** The with of the panel that holds the pathLabel and clearBtn. */
    public static final int PADDING_FOR_PATH_LABEL = 10;

    /** The with of the panel that holds the pathLabel and clearBtn. */
    public static final int FOLDER_PATH_WIDTH = 300;

    /* ===== Colors ===== */

    /** A light theme color for uniform appearance. */
    public static final Color CLR_LIGHT = new Color(254, 253, 239);

    /* ===== Dimensions ===== */

    /**
     * The dimension of the app.
     * <p>width: 900, height: 600</p>
     */
    public static final Dimension APP_DIMENSION = new Dimension(900, 600);

    /** A dimension for JDialogs. */
    public static final Dimension DIALOG_DIMENSION = new Dimension(400, 220);

    /*=========================
    * Model
    =========================*/

    /** A general error message used if something that shouldn't happen happens. */
    public static final String GENERAL_ERROR = "<html>Something went wrong when trying to access the folder. We are"
    + " sorry for this and will look into the issue as soon as possible<html>";
}

package com.dt042g.photochronicle.support;

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

    /* ===== Numbers ===== */

    /** An integer that defines hgap or vgap  between components in a panel that has FlowLayout. */
    public static final int FLOW_GAP = 15;


    /** An integer that defines the size of the title text. */
    public static final int TEXT_SIZE_TITLE = 28;

    /* ===== Dimensions ===== */

    /**
     * The dimension of the app.
     * <p>width: 900, height: 600</p>
     */
    public static final Dimension APP_DIMENSION = new Dimension(900, 600);
}

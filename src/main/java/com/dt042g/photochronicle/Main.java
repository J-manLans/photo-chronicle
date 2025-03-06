package com.dt042g.photochronicle;

import com.dt042g.photochronicle.controller.ChronicleController;

/**
 * The entry point for the PhotoChronicle application.
 * @author Joel Lansgren
 */
public final class Main {
    private Main() { // Private constructor to prevent instantiation.
        throw new IllegalStateException("Utility class");
    }

    /**
     * Instantiates and initializes the {@link ChronicleController}.
     * @param args command arguments (not used for this application).
     */
    public static void main(final String... args) {
        new ChronicleController().initialize();
    }
}

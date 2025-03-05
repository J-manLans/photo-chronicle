package com.dt042g.photochronicle.support;

/**
 * A utility class that provides configuration constants for the photo chronicler application.
 *
 * <p>This class is designed to hold static configuration values related to the sorting of image
 * folders by year and month. The main purpose of this class is to centralize and manage
 * constants or configuration values for the application.</p>
 *
 * <p>The {@link AppConfig} class cannot be instantiated, as it is intended purely for static use.</p>
 *
 * @author Joel Lansgren
 */
public final class AppConfig {

    private AppConfig() { // Private constructor to prevent instantiation.
        throw new IllegalStateException("Utility class");
    }
}

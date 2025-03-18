package com.dt042g.photochronicle.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public final class ChronicleModel {
    private Path path;

    /**
     * Sets the path variable.
     * @param path the path to be set on the path.
     */
    public void setPath(final String path) {
        this.path = Paths.get(path);
    }

    public void sortFolder(final Consumer<String> displayError) {

    }
}

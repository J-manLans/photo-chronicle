package com.dt042g.photochronicle.model;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import com.dt042g.photochronicle.support.AppConfig;

public final class ChronicleModel {
    private Path path;
    private String writeError;

    /**
     * Sets the path variable and creates various error messages depending on the path via {@link #setErrorMessages}.
     * @param path the path to be set on the path.
     */
    public void setPath(final String path) {
        this.path = Paths.get(path);
        setErrorMessages();
    }

    public void sortFolder(final Consumer<String> displayError) {
        try {
            verifyAccess();
        } catch (AccessDeniedException e) {
            System.err.println(e);
            displayError.accept(e.getMessage());
            return;
        }
    }

    /**
     * Verifies whether access to the folder is allowed.
     *
     * <p>This method checks if the path exists, is a directory, and if the current user has
     * write permissions. It throws an {@code AccessDeniedException} if any of these checks
     * fail.</p>
     * @throws AccessDeniedException if the access to the folder is denied
     */
    void verifyAccess() throws AccessDeniedException {
        if (path != null && Files.exists(path) && Files.isDirectory(path)) {
            if (!Files.isWritable(path)) {
                throw new AccessDeniedException(writeError);
            }
        } else {
            throw new AccessDeniedException(AppConfig.GENERAL_ERROR);
        }
    }

    private void setErrorMessages() {
        writeError = String.format(
            "<html>Write access denied to folder:<br><i>%s</i>.<br>Select a different one or modify its"
            + " permissions by right-clicking and selecting <i>Properties</i>.<html>", path
        );
    }

    /**
     * Nullifies the path, used to reset the test environment.
     */
    void nullifyPath() {
        path = null;
    }
}

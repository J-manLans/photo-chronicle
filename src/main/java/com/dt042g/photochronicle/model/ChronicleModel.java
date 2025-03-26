package com.dt042g.photochronicle.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.dt042g.photochronicle.support.AppConfig;

/**
 * The {@code ChronicleModel} class is responsible for organizing images in a specified folder
 * by analyzing their EXIF metadata. It sorts images into subdirectories based on the year
 * and month of the original date the photo was taken.
 *
 * <p>The class performs the following operations:
 * <ul>
 *     <li>Verifies access permissions for the selected folder.</li>
 *     <li>Scans the folder for image files.</li>
 *     <li>Extracts EXIF metadata to determine the original date.</li>
 *     <li>Organizes images into year/month subdirectories.</li>
 *     <li>Handles errors such as access denial, missing metadata, and directory creation failures.</li>
 * </ul>
 *
 * <p>Statistics are collected on sorted, unsorted, and invalid files.
 * The sorting process results in a summary message for display in a UI.</p>
 *
 * @author Joel Lansgren, Daniel Berg
 */
public final class ChronicleModel {
    private Path path;
    private final Map<Integer, Map<Integer, List<String>>> eligibleFiles = new HashMap<>();

    private enum StatsIndex {
        sortedFiles,
        unsortedFiles,
        directoryFailures,
        invalidFiles
    }

    private final int[] statistics = new int[StatsIndex.values().length];

    /**
     * Sets the path variable and creates various error messages depending on the path via {@link #setErrorMessages}.
     * @param path the path to be set on the path.
     */
    public void setPath(final String path) {
        this.path = Paths.get(path);
    }

    /**
     * Used to sort images of the selected folder by looking up EXIF metadata. The images will be sorted into
     * subdirectories based on the year and month of the original date.
     * @param displayError callback method to display error message.
     * @param displayInformation callback method to display information.
     */
    public void sortFolder(final Consumer<String> displayError, final Consumer<String> displayInformation) {
        try {
            verifyAccess();
        } catch (AccessDeniedException | NoSuchFileException | NotDirectoryException e) {
            System.err.println(e);
            displayError.accept(e.getMessage());
            return;
        }

        reset();

        try (Stream<Path> directoryContents = Files.list(path)) {
            directoryContents
                    .filter(file -> !Files.isDirectory(file))
                    .forEach(file -> detectEXIFMetadataFiles(file.toFile()));
        } catch (final IOException e) {
            System.err.println(e);
            displayError.accept("Failed to process the directory. Please check the path and try again.");
            return;
        }

        moveEligibleFiles();

        if (statistics[StatsIndex.sortedFiles.ordinal()] == 0) {
            displayError.accept(AppConfig.NO_FILES_SORTED);
        } else {
            displayInformation.accept(getMessageStatistics());
        }
    }

    /**
     * Used to create and return a message with the current statistics.
     * @return A message with the current statistics.
     */
    String getMessageStatistics() {
        return "<html>Sorting of directory " + path + " has finished.<br>Statistics:<br>"
                + "Number of files sorted: "
                + statistics[StatsIndex.sortedFiles.ordinal()] + "<br>"
                + "Number of files which couldn't be sorted: "
                + statistics[StatsIndex.unsortedFiles.ordinal()] + "<br>"
                + "Number of directory creation failures: "
                + statistics[StatsIndex.directoryFailures.ordinal()] + "<br>"
                + "Number of invalid files: "
                + statistics[StatsIndex.invalidFiles.ordinal()] + "<br>"
                + "</html>";
    }

    /**
     * Verifies whether access to the folder is allowed.
     *
     * <p>This method checks if the path exists, is a directory, and if the current user has
     * write permissions. It throws an exception if any of these checks
     * fail.</p>
     * @throws AccessDeniedException if the access to the folder is denied
     * @throws NoSuchFileException if the directory don't exists
     * @throws NotDirectoryException if the path is not a directory
     */
    void verifyAccess() throws AccessDeniedException, NoSuchFileException, NotDirectoryException {
        if (path == null) {
            throw new NoSuchFileException(AppConfig.GENERAL_ERROR);
        } else if (!Files.exists(path)) {
            throw new NoSuchFileException(AppConfig.GENERAL_ERROR);
        } else if (!Files.isDirectory(path)) {
            throw new NotDirectoryException(AppConfig.GENERAL_ERROR);
        } else if (!Files.isWritable(path)) {
            throw new AccessDeniedException(setErrorMessage("Write"));
        } else if (!Files.isReadable(path)) {
            throw new AccessDeniedException(setErrorMessage("Read"));
        }
    }

    /**
     * Sets an error message that defines what type of access is denied to a path.
     * @param type the type of access that is denied
     * @return a string in html format for nice displaying in a JDialog
     */
    String setErrorMessage(final String type) {
        return String.format(
                "<html>%s access denied to folder:<br><i>%s</i>.<br>Select a different one or modify its"
                        + " permissions by right-clicking and selecting <i>Properties</i>.<html>", type, path
        );
    }

    /**
     * Nullifies the path, used to reset the test environment.
     */
    void nullifyPath() {
        path = null;
    }

    private void detectEXIFMetadataFiles(final File file) {
        try {
            final Metadata metadata = ImageMetadataReader.readMetadata(file);
            final ExifSubIFDDirectory exifSubIFDDirectory = metadata
                    .getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (exifSubIFDDirectory == null) {
                statistics[StatsIndex.invalidFiles.ordinal()]++;
                return;
            }

            final Date originalDate = exifSubIFDDirectory.getDateOriginal();

            if (originalDate == null) {
                statistics[StatsIndex.invalidFiles.ordinal()]++;
                return;
            }

            final LocalDate date = originalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            addEligibleFile(file.getName(), date.getYear(), date.getMonthValue());
        } catch (ImageProcessingException | IOException e) {
            statistics[StatsIndex.invalidFiles.ordinal()]++;
        }
    }

    private void addEligibleFile(final String file, final int year, final int month) {
        if (eligibleFiles.get(year) == null) {
            final Map<Integer, List<String>> yearData = new HashMap<>();
            eligibleFiles.put(year, yearData);
        }

        if (eligibleFiles.get(year).get(month) == null) {
            final List<String> monthData = new ArrayList<>();
            eligibleFiles.get(year).put(month, monthData);
        }

        eligibleFiles.get(year).get(month).add(file);
    }

    private void moveEligibleFiles() {
        Path basePath = Paths.get(path.toString());

        eligibleFiles.forEach((year, months) -> {
            Path directoryYear = basePath.resolve(year.toString());

            months.forEach((month, files) -> {
                final String strMonth = String.format("%02d-%s", month, AppConfig.MONTHS[month - 1]);
                Path directoryMonth = directoryYear.resolve(strMonth);
                final File fileMonth = directoryMonth.toFile();

                if (!fileMonth.exists() && !fileMonth.mkdirs()) {
                    statistics[StatsIndex.directoryFailures.ordinal()]++;
                } else {
                    files.forEach((file) -> moveFile(
                            Paths.get(path.toString(), file),
                            Paths.get(fileMonth.toString(), file)));
                }
            });
        });
    }

    private void moveFile(final Path source, final Path destination) {
        try {
            Files.move(source, destination);
            statistics[StatsIndex.sortedFiles.ordinal()]++;
        } catch (final IOException e) {
            statistics[StatsIndex.unsortedFiles.ordinal()]++;
        }
    }

    private void reset() {
        eligibleFiles.clear();
        Arrays.fill(statistics, 0);
    }
}

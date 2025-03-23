package com.dt042g.photochronicle.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link ChronicleModel}, ensuring design integrity and correct functionality.
 * @author Joel Lansgren
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChronicleModelTest {
    private final ChronicleModel model = new ChronicleModel();
    private final Class<?> modelClass = model.getClass();
    private final String pathToTestFolder = Paths.get(
        System.getProperty("user.dir"), "src", "test", "resources", "testImageFolder"
    ).toString();
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "path", "eligibleFiles", "statistics"
    ));
    private AclFileAttributeView aclView;
    private List<AclEntry> originalAcl;
    private final String pathToSort = Paths.get(
            System.getProperty("user.dir"), "src", "test", "resources", "testSort"
    ).toString();
    private final String[] nameOfMonths = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };
    private final Map<Integer, Map<Integer, List<String>>> testFiles = Map.of(
            2025, Map.of(3, List.of("001.jpg", "002.jpg")),
            2024, Map.of(8, List.of("003.jpg")),
            2023, Map.of(6, List.of("004.jpg", "005.jpg"))
    );

    /*====================
    * Setup
    *====================*/

    @AfterEach
    private void tearDown() throws IOException {
        if (originalAcl != null && aclView != null) {
            aclView.setAcl(originalAcl);
            originalAcl = null;
            aclView = null;
        }
        model.nullifyPath();

        resetTestFolder();
    }

    /*========================
    * Design Integrity Tests
    ========================*/

    /**
     * Test to ensure that the class is public, so it can be instantiated from another package.
     */
    @Test
    void shouldPassIfChronicleModelIsPublic() {
        assertTrue(Modifier.isPublic(modelClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    void shouldPassIfChronicleModelIsFinal() {
        assertTrue(Modifier.isFinal(modelClass.getModifiers()));
    }

    /**
     * Checks that all actual fields exist among the expected ones.
     * @param fieldName the name of the instance field.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfChronicleModelHasNoExtraFields(final String fieldName) {
        assertTrue(expectedFields.contains(fieldName));
    }

    /**
     * Checks that all expected fields are present among the actual ones.
     * @return A stream of dynamic tests, each testing whether an expected field exists in the ChronicleModel's fields.
     */
    @TestFactory
    Stream<DynamicTest> shouldPassIfChronicleModelHasNoMissingFields() {
        final List<String> actualFields = provideClassFields().collect(Collectors.toList());

        return expectedFields.stream()
        .map(field -> DynamicTest.dynamicTest("Check field: " + field, () -> {
            assertTrue(actualFields.contains(field));
        }));
    }

    /**
     * Test to ensure that all fields have private access modifier.
     * @param fieldName the name of the instance field.
     * @throws NoSuchFieldException if an instance field is not present.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfAllInstanceFieldsArePrivate(final String fieldName) throws NoSuchFieldException {
        assertTrue(Modifier.isPrivate(modelClass.getDeclaredField(fieldName).getModifiers()));
    }

    /**
     * Test to ensure that no final instance fields are null.
     * @param fieldName the name of the instance field.
     * @throws NoSuchFieldException if an instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfNoFieldIsNull(final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (Modifier.isFinal(getField(fieldName).getModifiers())) {
            assertNotNull(getComponent(fieldName));
        }
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Tests that the Path is successfully set through the setPath method.
     */
    @Test
    void shouldHaveThePathSet() {
        model.setPath(pathToTestFolder);
        assertEquals(pathToTestFolder, getComponent("path").toString());
    }

    /**
     * Tests that the setErrorMessage returns the right string.
     */
    @Test
    void shouldReturnCorrectErrorMessage() {
        model.setPath("no folder");
        assertEquals(
            "<html>Test access denied to folder:<br><i>no folder</i>.<br>Select a different one or modify its"
            + " permissions by right-clicking and selecting <i>Properties</i>.<html>",
            model.setErrorMessage("Test")
        );
    }

    /**
     * Tests that the sortFolder method have a Consumer<String> parameter and nothing else.
     * @throws NoSuchMethodException if the sortFolder method is not present.
     */
    @Test
    void shouldHaveAConsumerStringAsParameter() throws NoSuchMethodException {
        // Confirms the method have one parameter and that it's a consumer
        final Method method = modelClass.getDeclaredMethod("sortFolder", Consumer.class, Consumer.class);
        final Parameter[] parameters = method.getParameters();
        Type parameterType = null;
        String parameterTypeName = null;

        for (final Parameter parameter : parameters) {
            parameterType = parameter.getParameterizedType();
            parameterTypeName = parameterType.getTypeName();
        }

        // And here we check if the parameters type name contains "String"
        assertTrue(parameterTypeName.contains("String"));
    }

    /**
     * Test that verifies the behavior when a null folder path is provided.
     *
     * This test ensures that if the folder path is null, an AccessDeniedException is thrown when
     * the model's verifyAccess() method is invoked.
     */
    @Test
    void shouldThrowForNullFolder() {
        assertThrows(IllegalArgumentException.class, () -> model.verifyAccess());
    }

    /**
     * Test that verifies the behavior when a valid folder path is provided.
     *
     * This test ensures that if the folder path is set to a valid folder, the model's verifyAccess()
     * method does not throw any exception.
     */
    @Test
    void shouldNotThrowForValidFolder() {
        model.setPath(pathToTestFolder);
        assertDoesNotThrow(() -> model.verifyAccess());
    }

    /**
     * Test that verifies the behavior for an invalid folder on Windows.
     *
     * This test ensures that if the folder path is set to a valid folder but the write permissions
     * are explicitly denied for "Everyone" on Windows, an AccessDeniedException is thrown when
     * the model's verifyAccess() method is invoked. It configures ACL (Access Control List) entries
     * to deny write permissions through {@link #setReadOnlyAccess} and checks that the exception is properly thrown.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldThrowForInvalidValidFolder() {
        model.setPath(pathToTestFolder);
        setFolderAccess(AclEntryPermission.WRITE_DATA);

        assertThrows(AccessDeniedException.class, () -> model.verifyAccess());
    }

    /**
     * Test that verifies the behavior for an invalid folder on Windows.
     *
     * This test ensures that if the folder path is set to a valid folder but the write permissions
     * are explicitly denied for "Everyone" on Windows, an AccessDeniedException is thrown when
     * the model's verifyAccess() method is invoked. It configures ACL (Access Control List) entries
     * to deny write permissions through {@link #setReadOnlyAccess} and checks that the exception is properly thrown.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldThrowForReadAccessToFolder() {
        model.setPath(pathToTestFolder);
        setFolderAccess(AclEntryPermission.READ_DATA);

        assertThrows(AccessDeniedException.class, () -> model.verifyAccess());
    }

    /**
     * Test that verifies that the error message is empty when the folder is valid.
     */
    @Test
    void shouldHaveEmptyErrorMessageForValidFolder() {
        model.setPath(pathToTestFolder);
        assertEquals("", getErrorFromVerifyMethod());
    }

    /**
     * Test that verifies the handling of an empty path in the sortFolder method.
     */
    @Test
    void shouldHandleEmptyPathInSortFolderMethod() {
        assertThrows(IllegalArgumentException.class, () -> performSortingTest(null));
    }

    /**
     * Test that verifies the handling of a write error in the sortFolder method.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldHandleWriteErrorInSortFolderMethod() {
        model.setPath(pathToTestFolder);
        setFolderAccess(AclEntryPermission.WRITE_DATA);

        assertEquals(model.setErrorMessage("Write"), getErrorFromVerifyMethod());
    }

    /**
     * Test that verifies the handling of a write error in the sortFolder method.
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldHandleReadErrorInSortFolderMethod() {
        model.setPath(pathToTestFolder);
        setFolderAccess(AclEntryPermission.READ_DATA);

        assertEquals(model.setErrorMessage("Read"), getErrorFromVerifyMethod());
    }

    /**
     * Ensure that all directories for the years have been created correctly.
     */
    @Test
    public void shouldPassIfAllYearDirectoriesExist() {
        performSortingTest(pathToSort);
        assertTrue(isEveryYearDirectoryPresent());
    }

    /**
     * Ensure that all directories for the months have been created correctly.
     */
    @Test
    public void shouldPassIfAllMonthDirectoriesExist() {
        performSortingTest(pathToSort);
        assertTrue(isEveryMonthDirectoryPresent());
    }

    /**
     * Ensure that all files have been moved correctly.
     */
    @Test
    public void shouldPassIfFilesAreMovedCorrectly() {
        performSortingTest(pathToSort);
        assertTrue(isEveryMovedFilesPresent());
    }

    /*======================
    * Helper Methods
    ======================*/

    private Stream<String> provideClassFields() {
        return Arrays.stream(modelClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(final String fieldName) {
        try {
            final Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(model);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }

    private void setFolderAccess(final AclEntryPermission deniedAccess) {
        try {
            final Path folder = (Path) getComponent("path");

            // Get Access Control List (ACL) view
            aclView = Files.getFileAttributeView(folder, AclFileAttributeView.class);

            originalAcl = aclView.getAcl(); // Stores original for restoration in tearDown
            final List<AclEntry> acl = aclView.getAcl(); // Get current ACL

            // Create a deny write permission entry for Everyone
            final AclEntry denyWrite = AclEntry.newBuilder()
            .setType(AclEntryType.DENY)
            .setPrincipal(FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("Everyone"))
            .setPermissions(deniedAccess)
            .build();

            // Insert DENY first to ensure it takes precedence,
            // as Windows stops checking after the first matching rule.
            acl.add(0, denyWrite);
            aclView.setAcl(acl); // Sets the acl.
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to set read-only access", e);
        }
    }

    /**
     * Retrieves the error message generated by the verifyAccess method when invoked through sortFolder.
     *
     * This helper method calls the model's sortFolder method and captures any error message that is displayed
     * by the verifyAccess method. The error message is returned as a string.
     *
     * @return the error message returned by the verifyAccess method, or an empty string if no error occurred.
     */
    private String getErrorFromVerifyMethod() {
        final StringBuilder errorMessage = new StringBuilder();
        final StringBuilder information = new StringBuilder();
        final Consumer<String> displayError = errorMessage::append;
        final Consumer<String> displayInformation = information::append;

        model.sortFolder(displayError, displayInformation);

        return errorMessage.toString();
    }

    private void performSortingTest(final String pathToFolder) {
        final StringBuilder errorMessage = new StringBuilder();
        final StringBuilder information = new StringBuilder();
        final Consumer<String> displayError = errorMessage::append;
        final Consumer<String> displayInformation = information::append;

        if (pathToFolder != null) { model.setPath(pathToFolder); }
        model.sortFolder(displayError, displayInformation);
    }

    private void resetTestFolder() {
        testFiles.forEach((year, months) -> {
            final String strYear = year.toString();
            months.forEach((month, files) -> {
                final String strMonth = String.format("%02d-%s", month, nameOfMonths[month - 1]);

                files.forEach(file -> {
                    final Path source = Paths.get(pathToSort, strYear, strMonth, file);
                    final Path destination = Paths.get(pathToSort, file);

                    if (Files.exists(source)) {
                        try {
                            Files.move(source, destination);
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                deleteFolder(Paths.get(pathToSort, strYear, strMonth));
            });

            deleteFolder(Paths.get(pathToSort, strYear));
        });
    }

    private void deleteFolder(final Path path) {
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEveryYearDirectoryPresent() {
        final List<Boolean> filesExists = new ArrayList<>();

        testFiles.forEach((year, months) -> {
            final String strYear = year.toString();
            filesExists.add(Files.exists(Paths.get(pathToSort, strYear)));
        });

        return !filesExists.contains(false);
    }

    private boolean isEveryMonthDirectoryPresent() {
        final List<Boolean> filesExists = new ArrayList<>();

        testFiles.forEach((year, months) -> {
            final String strYear = year.toString();
            months.forEach((month, files) -> {
                final String strMonth = String.format("%02d-%s", month, nameOfMonths[month - 1]);
                filesExists.add(Files.exists(Paths.get(pathToSort, strYear, strMonth)));
            });
        });

        return !filesExists.contains(false);
    }

    private boolean isEveryMovedFilesPresent() {
        final List<Boolean> filesExists = new ArrayList<>();

        testFiles.forEach((year, months) -> {
            final String strYear = year.toString();
            months.forEach((month, files) -> {
                final String strMonth = String.format("%02d-%s", month, nameOfMonths[month - 1]);

                files.forEach(file -> {
                    filesExists.add(Files.exists(Paths.get(pathToSort, strYear, strMonth, file)));
                });
            });
        });

        return !filesExists.contains(false);
    }
}

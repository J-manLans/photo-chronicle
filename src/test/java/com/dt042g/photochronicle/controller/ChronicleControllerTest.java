package com.dt042g.photochronicle.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.dt042g.photochronicle.model.ChronicleModel;
import com.dt042g.photochronicle.support.AppConfig;
import com.dt042g.photochronicle.view.BottomPanel;
import com.dt042g.photochronicle.view.InfoDialog;
import com.dt042g.photochronicle.view.MainFrame;
import com.dt042g.photochronicle.view.MiddlePanel;

/**
 * Unit tests for the {@link ChronicleController} class.
 *
 * This test suite verifies the core functionalities of the controller, including
 * interactions with the model and view components. It ensures that event handling,
 * user interactions, and internal state updates function as expected.
 * @author Joel Lansgren
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChronicleControllerTest {
    private ChronicleController controller;
    private Class<?> controllerClass;
    private MainFrame mainFrame;
    private MiddlePanel middlePanel;
    private BottomPanel bottomPanel;
    private InfoDialog infoDialog;
    private ChronicleModel chronicleModel;
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "topPanel", "middlePanel", "bottomPanel", "mainFrame", "infoDialog", "chronicleModel"
    ));

    /*======================
    * Setup
    ======================*/

    @BeforeAll
    private void setup() {
        runOnEDT(() -> {
            controller = new ChronicleController();
            controller.initializeListeners();
            mainFrame = (MainFrame) getComponent(controller, "mainFrame");
            middlePanel = (MiddlePanel) getComponent(controller, "middlePanel");
            bottomPanel = (BottomPanel) getComponent(controller, "bottomPanel");
            infoDialog = (InfoDialog) getComponent(controller, "infoDialog");
            chronicleModel = (ChronicleModel) getComponent(controller, "chronicleModel");

            infoDialog.setModal(false);
        });
        controllerClass = controller.getClass();
    }

    /*============================
    * Design Integrity Tests
    ============================*/

    /**
     * Test to ensure that the class is public, so it can be instantiated from another package.
     */
    @Test
    void shouldPassIfChronicleControllerIsPublic() {
        assertTrue(Modifier.isPublic(controllerClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    void shouldPassIfChronicleControllerIsFinal() {
        assertTrue(Modifier.isFinal(controllerClass.getModifiers()));
    }

    /**
     * Checks that all actual fields exist among the expected ones.
     * @param fieldName the name of the instance field.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfChronicleControllerHasNoExtraFields(final String fieldName) {
        assertTrue(expectedFields.contains(fieldName));
    }

    /**
     * Checks that all expected fields are present among the actual ones.
     * @return A stream of dynamic tests, each testing whether an expected field exists in
     * the ChronicleController's fields.
     */
    @TestFactory
    Stream<DynamicTest> shouldPassIfChronicleControllerHasNoMissingFields() {
        final List<String> actualFields = provideClassFields().collect(Collectors.toList());

        return expectedFields.stream()
        .map(field -> DynamicTest.dynamicTest("Check field: " + field, () -> {
            assertTrue(actualFields.contains(field));
        }));
    }

    /**
     * Test to ensure that all fields have private access modifier.
     * @param fieldName the name of the instance field.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfAllInstanceFieldsArePrivate(final String fieldName) {
        assertTrue(Modifier.isPrivate(getField(controllerClass, fieldName).getModifiers()));
    }

    /**
     * Verifies that ChronicleController only has one constructor.
     */
    @Test
    void shouldPassIfChronicleControllerOnlyHasOneConstructor() {
        assertTrue(controllerClass.getDeclaredConstructors().length == 1);
    }

    /**
     * Verifies that ChronicleController's constructor is public.
     * @throws NoSuchMethodException if the constructor is not found in the ChronicleController class.
     */
    @Test
    void shouldReturnTrueIfConstructorModifierIsPublic() throws NoSuchMethodException {
        assertTrue(Modifier.isPublic(controllerClass.getDeclaredConstructor().getModifiers()));
    }

    /**
     * Checks that the ChronicleController constructors parameters equals 0.
     * @throws NoSuchMethodException if the constructor is not found in the ChronicleController class.
     */
    @Test
    void shouldPassIfConstructorTakeNoParameters() throws NoSuchMethodException {
        assertEquals(0, controllerClass.getDeclaredConstructor().getParameterCount());
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Tests that the showMainFrame method makes the main frame visible.
     */
    @Test
    void shouldShowMainframe() {
        runOnEDT(() -> {
            mainFrame.setVisible(false);
            controller.showMainFrame();
            assertTrue(mainFrame.isVisible());
            mainFrame.setVisible(false);
        });
    }

    /*======================
    * Integration Tests
    ======================*/

    /* ===== BottomPanel - InfoDialog ===== */

    /**
     * Tests that the dialog opens up when the info button is pressed.
     */
    @Test
    void shouldOpenDialogWhenInfoButtonIsPressed() {
        runOnEDT(() -> ((JButton) getComponent(bottomPanel, "infoButton")).doClick());
        assertTrue(infoDialog.isVisible());
        runOnEDT(() -> infoDialog.setVisible(false));
    }

    /**
     * Tests that the dialog opens up in the center of the MainFrame.
     */
    @Test
    void shouldPassIfDialogOpensUpInCenterOfApp() {
        final Random randomizer = new Random();
        final Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final int availableScreenWidth =  (int) screenBounds.getWidth() - mainFrame.getWidth();
        final int availableScreenHeight =  (int) screenBounds.getHeight() - mainFrame.getHeight();
        final int x = randomizer.nextInt(availableScreenWidth);
        final int y = randomizer.nextInt(availableScreenHeight);

        runOnEDT(() -> {
            mainFrame.setLocation(x, y);
            mainFrame.setVisible(true);
            ((JButton) getComponent(bottomPanel, "infoButton")).doClick();
        });

        final Point mainFrameTopLeftPoint = mainFrame.getLocationOnScreen();

        final int frameWidth = mainFrame.getWidth();
        final int frameHeight = mainFrame.getHeight();

        final int dialogWidth =  infoDialog.getWidth();
        final int dialogHeight =  infoDialog.getHeight();

        final Point expectedTopLeftPoint = new Point(
            Math.round(mainFrameTopLeftPoint.x + ((frameWidth - dialogWidth) / 2)),
            Math.round(mainFrameTopLeftPoint.y + ((frameHeight - dialogHeight) / 2))
        );

        assertEquals(expectedTopLeftPoint, infoDialog.getLocationOnScreen());
        runOnEDT(() -> {
            infoDialog.setVisible(false);
            mainFrame.setVisible(false);
        });
    }

    /**
     * Tests that the dialog is closed when the infoCloseBtn is clicked.
     */
    @Test
    void shouldPassIfDialogIsClosedWhenCloseBtnIsClicked() {
        boolean isVisible = true;
        runOnEDT(() -> ((JButton) getComponent(bottomPanel, "infoButton")).doClick());

        if (infoDialog.isVisible()) {
            runOnEDT(() -> ((JButton) getComponent(infoDialog, "infoCloseBtn")).doClick());
            isVisible  = infoDialog.isVisible();
        }

        assertFalse(isVisible);
        runOnEDT(() -> infoDialog.setVisible(false));
    }

    /* ===== MiddlePanel - ChronicleModel ===== */

    /**
     * Ensures that selecting a folder sets a path in the model.
     */
    @Test
    void shouldSetPathInModelWhenSelectingFolder() {
        invokeRobotKeyPress(KeyEvent.VK_ENTER);
        runOnEDT(() -> ((JButton) getComponent(middlePanel, "chooseFolderBtn")).doClick());
        final JLabel pathLabel = (JLabel) getComponent(middlePanel, "pathLabel");
        final Path path = (Path) getComponent(chronicleModel, "path");

        assertEquals(pathLabel.getText(), path.toString());
    }

    /**
     * Ensures that canceling a folder selection don't set a path in the model.
     */
    @Test
    void shouldNotSetPathInModelWhenCancelingSelectingFolder() {
        invokeRobotKeyPress(KeyEvent.VK_ESCAPE);
        runOnEDT(() -> ((JButton) getComponent(middlePanel, "chooseFolderBtn")).doClick());
        assertEquals(null, getComponent(chronicleModel, "path"));
    }

    /* ===== ChronicleModel - InfoDialog ===== */

    /**
     * Ensures that a error message is displayed in the info dialog for a path that is not a folder.
     */
    @Test
    void shouldDisplayErrorDialogWithGeneralErrorMessage() {
        runOnEDT(() -> {
            controller.sortFolder("not-a-folder");
            assertEquals(
                AppConfig.GENERAL_ERROR,
                ((JLabel) getComponent(infoDialog, "infoMessage")).getText()
            );
            infoDialog.setVisible(false);
            ((JLabel) getComponent(middlePanel, "pathLabel")).setForeground(AppConfig.CLR_PATH_LABEL);
        });
    }

    /**
     * Ensures that the pathLabel turns red when a path generates an error.
     */
    @Test
    void shouldColorPathLabelRedWhenFaultyPathIsGiven() {
        runOnEDT(() -> {
            controller.sortFolder("not-a-folder");
            assertEquals(Color.RED, ((JLabel) getComponent(middlePanel, "pathLabel")).getForeground());
            infoDialog.setVisible(false);
            ((JLabel) getComponent(middlePanel, "pathLabel")).setForeground(AppConfig.CLR_PATH_LABEL);
        });
    }

    /**
     * Ensures that a info message is displayed in the info dialog for a path that is a valid folder.
     */
    @Test
    void shouldDisplayInformationDialogIfSortingGoesWell() {
        final String pathToTestFolder = Paths.get(
            System.getProperty("user.dir"), "src", "test", "resources", "testImageFolder"
        ).toString();

        runOnEDT(() -> {
            controller.sortFolder(pathToTestFolder);
            assertEquals(AppConfig.NO_FILES_SORTED,
                    ((JLabel) getComponent(infoDialog, "infoMessage")).getText());
            infoDialog.setVisible(false);
        });
    }

    /*======================
    * Helper Methods
    ======================*/

    private Stream<String> provideClassFields() {
        return Arrays.stream(controllerClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final Object classInstance, final String fieldName) {
        try {
            return getField(classInstance.getClass(), fieldName).get(classInstance);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }

    private void invokeRobotKeyPress(final int key) {
        try {
            final Robot robot =  new Robot();
            final int initialWaitTime = 250;

            new Thread(() -> {
                robot.delay(initialWaitTime);
                robot.keyPress(key);
                robot.keyRelease(key);
            }).start();
        } catch (final AWTException e) {
            throw new IllegalStateException("Failed to initialize Robot for key press: " + key, e);
        }
    }

    private void runOnEDT(final Runnable action) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                action.run();
            });
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted while executing action on EDT.", e);
        } catch (final InvocationTargetException e) {
            throw new IllegalStateException("Failed to execute action on the EDT: " + action, e);
        }
    }
}

package com.dt042g.photochronicle.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "topPanel", "middlePanel", "bottomPanel", "mainFrame", "infoDialog", "chronicleModel"
    ));

    /*======================
    * Setup
    ======================*/

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            controller = new ChronicleController();
            controller.initializeListeners();
            controller.getInfoDialog().setModal(false);
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
     * @throws NoSuchFieldException if an instance field is not present.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfAllInstanceFieldsArePrivate(final String fieldName) throws NoSuchFieldException {
        assertTrue(Modifier.isPrivate(controllerClass.getDeclaredField(fieldName).getModifiers()));
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
    void shouldPassIfConstructorParametersEqualsComponentCount() throws NoSuchMethodException {
        assertEquals(0, controllerClass.getDeclaredConstructor().getParameterCount());
    }

    /*======================
    * Integration Tests
    ======================*/

    /* ===== BottomPanel - InfoDialog ===== */

    /**
     * Tests that the dialog opens up when the info button is pressed.
     * @throws InvocationTargetException If the method invoked by reflection throws an exception.
     * @throws InterruptedException If the thread is interrupted while waiting for the EDT to process the action.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    void shouldOpenDialogWhenInfoButtonIsPressed()
    throws InvocationTargetException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        InfoDialog infoDialog = controller.getInfoDialog();
        clickInfoButton();
        assertTrue(infoDialog.isVisible());
        SwingUtilities.invokeAndWait(() -> infoDialog.setVisible(false));
    }

    /**
     * Tests that the dialog opens up in the center of the MainFrame.
     * @throws InvocationTargetException If the method invoked by reflection throws an exception.
     * @throws InterruptedException If the thread is interrupted while waiting for the EDT to process the action.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    void shouldPassIfDialogOpensUpInCenterOfApp()
    throws InvocationTargetException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        InfoDialog infoDialog = controller.getInfoDialog();
        MainFrame mainFrame = controller.getMainFrame();

        mainFrame.setVisible(true);
        clickInfoButton();

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
        SwingUtilities.invokeAndWait(() -> {
            infoDialog.setVisible(false);
            mainFrame.setVisible(false);
        });
    }

    /**
     * Tests that the dialog is closed when the infoCloseBtn is clicked.
     * @throws InvocationTargetException If the method invoked by reflection throws an exception.
     * @throws InterruptedException If the thread is interrupted while waiting for the EDT to process the action.
     */
    @Test
    void shouldPassIfDialogIsClosedWhenCloseBtnIsClicked() throws InvocationTargetException, InterruptedException {
        final InfoDialog infoDialog = controller.getInfoDialog();
        final JButton infoCloseBtn = (JButton) getComponent(controller.getInfoDialog(), "infoCloseBtn");
        boolean isVisible = true;
        clickInfoButton();

        if (infoDialog.isVisible()) {
            SwingUtilities.invokeAndWait(() -> infoCloseBtn.doClick());
            isVisible  = infoDialog.isVisible();
        }

        assertFalse(isVisible);
        SwingUtilities.invokeAndWait(() -> infoDialog.setVisible(false));
    }

    /* ===== MiddlePanel - ChronicleModel ===== */

    /**
     * Ensures that selecting a folder sets a path in the model.
     */
    @Test
    void shouldSetPathInModelWhenSelectingFolder() {
        clickChooseFolderWithKeyAction(KeyEvent.VK_ENTER);
        final JLabel pathLabel = (JLabel) getComponent(controller.getMiddlePanel(), "pathLabel");
        final Path path = (Path) getComponent(controller.getChronicleModel(), "path");

        assertEquals(pathLabel.getText(), path.toString());
    }

    /**
     * Ensures that canceling a folder selection don't set a path in the model.
     */
    @Test
    void shouldNotSetPathInModelWhenCancelingSelectingFolder() {
        clickChooseFolderWithKeyAction(KeyEvent.VK_ESCAPE);
        assertEquals(null, getComponent(controller.getChronicleModel(), "path"));
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

    private void clickChooseFolderWithKeyAction(final int key) {
        final MiddlePanel middlePanel = controller.getMiddlePanel();
        final JButton chooseFolderBtn = (JButton) getComponent(middlePanel, "chooseFolderBtn");

        invokeRobotKeyPress(key);

        try {
            SwingUtilities.invokeAndWait(() -> chooseFolderBtn.doClick());
        } catch (InvocationTargetException | InterruptedException e) {
            throw new IllegalStateException("Failed to perform a click on : " + chooseFolderBtn, e);
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

    private void clickInfoButton() {
        final BottomPanel bottomPanel = controller.getBottomPanel();
        final JButton infoButton = (JButton) getComponent(bottomPanel, "infoButton");

        try {
            SwingUtilities.invokeAndWait(() -> {
                infoButton.doClick();
            });
        } catch (InvocationTargetException | InterruptedException e) {
            throw new IllegalStateException("Failed to perform a click on : " + infoButton, e);
        }
    }
}

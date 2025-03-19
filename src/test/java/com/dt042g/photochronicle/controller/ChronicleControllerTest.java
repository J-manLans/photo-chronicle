package com.dt042g.photochronicle.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

    /*======================
    * Setup
    ======================*/

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            controller = new ChronicleController();
            controller.initializeListeners();
        });
        controllerClass = controller.getClass();
    }

    /*============================
    * Design Integrity Tests
    ============================*/

    /*======================
    * Unit Tests
    ======================*/

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

    private Stream<String> provideClassFields(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
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
}

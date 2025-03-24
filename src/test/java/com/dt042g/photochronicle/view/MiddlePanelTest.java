package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for the {@link MiddlePanel} class in the {@link com.dt042g.photochronicle.view} package.
 * These tests will verify the integrity of the {@link MiddlePanel} class, focusing on its layout,
 * UI components, field accessibility, and constructor.
 * @author Joel Lansgren
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MiddlePanelTest {
    private MiddlePanel middlePanel;
    private Class<?> middlePanelClass;

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            middlePanel = new MiddlePanel();
        });
        middlePanelClass = middlePanel.getClass();
    }

    /*============================
    * Design Integrity Tests
    ============================*/

    /**
     * Validates that the class is final.
     */
    @Test
    void shouldPassIfClassIsPublic() {
        assertTrue(Modifier.isPublic(middlePanelClass.getModifiers()));
    }

    /**
     * Validates that the class is final.
     */
    @Test
    void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(middlePanelClass.getModifiers()));
    }

    /**
     * Verifies that the class extends JPanel.
     */
    @Test
    void shouldPassIfClassExtendsJPanel() {
       assertEquals(JPanel.class, middlePanelClass.getSuperclass());
    }

    /**
     * Verifies that all instance fields are private.
     * @param fieldName
     * @throws NoSuchFieldException if the field is not present.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfAllInstanceFieldsArePrivate(final String fieldName) throws NoSuchFieldException {
        assertTrue(Modifier.isPrivate(middlePanelClass.getDeclaredField(fieldName).getModifiers()));
    }

    /**
     * Validates that all instance variables are instantiated.
     * @param fieldName
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldHaveInitializedFields(final String fieldName) {
        assertNotNull(getComponent(fieldName));
    }

    /**
     * Verifies that MiddlePanel only have one constructor.
     */
    @Test
    void shouldPassIfMiddlePanelHasOneConstructor() {
        assertTrue(1 == middlePanelClass.getDeclaredConstructors().length);
    }

    /**
     * Verifies that the constructor is public.
     * @throws NoSuchMethodException if the constructor is not found in the MiddlePanel class.
     */
    @Test
    void shouldPassIfConstructorModifierIsPublic() throws NoSuchMethodException {
        assertTrue(Modifier.isPublic(middlePanelClass.getDeclaredConstructor().getModifiers()));
    }

    /*==================================
    * Component Verification Tests
    ==================================*/

    /**
     * Validates that the MiddlePanel has a GridBagLayout.
     */
    @Test
    void shouldPassIfMiddlePanelHasGridBagLayout() {
        assertEquals(GridBagLayout.class, middlePanel.getLayout().getClass());
    }

    /**
     * Validates that the class contains one panel.
     */
    @Test
    void shouldPassIfMiddlePanelContainsOnePanel() {
        final Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JPanel.class));
    }

    /**
     * Validates that the class contains one label.
     */
    @Test
    void shouldPassIfMiddlePanelContainsOneLabel() {
        final Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JLabel.class));
    }

    /**
     * Validates that the class contains two buttons.
     */
    @Test
    void shouldPassIfMiddlePanelContainsTwoButtons() {
        final Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(2, componentCounts.get(JButton.class));
    }

    /**
     * Validates that the class contains one file chooser.
     */
    @Test
    void shouldPassIfMiddlePanelContainsOneFileChooser() {
        final Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JFileChooser.class));
    }

    /**
     * Tests if a JPanel is added to the MiddlePanel.
     */
    @Test
    void shouldPassIfAJPanelIsAddedToTheMiddlePanel() {
        assertEquals(1, provideAddedComponentsCount(middlePanel, JPanel.class));
    }

    /**
     * Verifies that the wrapper panel has a FlowLayout.
     */
    @Test
    void shouldPassIfWrapperPanelHasFlowLayout() {
        assertEquals(
            FlowLayout.class,
            ((JPanel) getComponent("labelAndClearBtnWrapper")).getLayout().getClass()
        );
    }

    /**
     * Verifies that the wrapper panel has a JLabel.
     */
    @Test
    void shouldPassIfAJLabelIsAddedToTheWrapperPanel() {
        assertEquals(1, provideAddedComponentsCount(
            (JPanel) getComponent("labelAndClearBtnWrapper"),
                JLabel.class
        ));
    }

    /**
     * Verifies that the wrapper panel has a JButton.
     */
    @Test
    void shouldPassIfAJButtonIsAddedToTheWrapperPanel() {
        assertEquals(1, provideAddedComponentsCount(
            (JPanel) getComponent("labelAndClearBtnWrapper"),
            JButton.class
        ));
    }

    /**
     * Tests if a JButton is added to the MiddlePanel.
     */
    @Test
    void shouldPassIfAJButtonIsAddedToTheMiddlePanel() {
        assertEquals(1, provideAddedComponentsCount(middlePanel, JButton.class));
    }

    /**
     * Validates that the fileChooser only displays folders in its dialog.
     */
    @Test
    void shouldPassIfJFileChooserOnlyDisplaysFolders() {
        assertEquals(
            JFileChooser.DIRECTORIES_ONLY,
            ((JFileChooser) getComponent("fileChooser")).getFileSelectionMode()
        );
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Ensure that a selected folder from the JFileChooser dialog updates the path label.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfSelectedFolderChangesFolderLabel()
    throws InvocationTargetException, InterruptedException, AWTException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");
        final JFileChooser fileChooser = (JFileChooser) getComponent("fileChooser");

        invokeRobotKeyPress(KeyEvent.VK_ENTER);
        SwingUtilities.invokeAndWait(() -> middlePanel.showFolderSelectionDialog(string -> { }));

        assertEquals(fileChooser.getSelectedFile().getAbsolutePath(), pathLabel.getText());
    }

    /**
     * Ensure that cancelling a selection of folder does not modify the path label.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfCancelFolderSelectionDoesNotModifyLabel()
    throws InvocationTargetException, InterruptedException, AWTException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");

        invokeRobotKeyPress(KeyEvent.VK_ESCAPE);
        SwingUtilities.invokeAndWait(() -> {
            pathLabel.setText(AppConfig.CHOOSE_FOLDER);
            middlePanel.showFolderSelectionDialog(string -> { });
        });

        assertEquals(AppConfig.CHOOSE_FOLDER, pathLabel.getText());
    }

    /**
     * Ensure that cancelling a selection of folder does not modify the path label.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfFolderSelectionRunsTheConsumer()
    throws InvocationTargetException, InterruptedException, AWTException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");
        final String testString = "test string";

        invokeRobotKeyPress(KeyEvent.VK_ENTER);
        SwingUtilities.invokeAndWait(() -> {
            middlePanel.showFolderSelectionDialog(string -> { pathLabel.setText(testString); });
        });

        assertEquals(testString, pathLabel.getText());
    }

    /**
     * Ensure that pressing the clear button resets the path label.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfClearButtonResetsThePathLabel() throws InvocationTargetException, InterruptedException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");
        final String dummy = "C:\\Foo\\Bar\\";

        SwingUtilities.invokeAndWait(() -> {
            pathLabel.setText(dummy);
            middlePanel.clearSelection();
        });

        assertEquals(AppConfig.CHOOSE_FOLDER, pathLabel.getText());
    }

    /**
     * Validates that the addListenerToFolderButton adds a listener to the folder button.
     */
    @Test
    void shouldAttachListenerViaAddListenerToFolderButton() {
        assertMethodAddsListener("chooseFolderBtn", middlePanel::addListenerToFolderButton);
    }

    /**
     * Validates that the addListenerToClearButton adds a listener to the clear button.
     */
    @Test
    void shouldAttachListenerViaAddListenerToClearButton() {
        assertMethodAddsListener("clearBtn", middlePanel::addListenerToClearButton);
    }

    /**
     * Validates that the text color of the label changes via the setErrorColorPath method.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     */
    @Test
    void shouldSetTextColorOnPathLabel() throws InvocationTargetException, InterruptedException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");
        final Color orgClr = pathLabel.getForeground();

        SwingUtilities.invokeAndWait(() -> middlePanel.setErrorColorPath());

        assertNotEquals(orgClr, pathLabel.getForeground());
    }

    /**
     * Validates that the resetPathColor method works.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     */
    @Test
    void shouldResetTextColorOnPathLabel() throws InvocationTargetException, InterruptedException {
        final JLabel pathLabel = (JLabel) getComponent("pathLabel");

        SwingUtilities.invokeAndWait(() -> {
            pathLabel.setForeground(Color.PINK);
            middlePanel.resetPathColor();
        });

        assertEquals(AppConfig.CLR_PATH_LABEL, pathLabel.getForeground());
    }

    /*============================
    * Helper methods
    ============================*/

    /**
     * Creates a stream from the fields array and filters out the variables that extend
     * {@link JComponent}. The {@code groupingBy} method that produces a map is then used
     * to group the fields by their type, counting how many times each unique type occurs
     * in the instance fields.
     * @return A map where the key is a class that extends {@link JComponent},
     * and the value is the number of times that class occurs in the instance fields.
     */
    private Map<Class<?>, Long> provideMapOfAllUIComponents() {
        return Arrays.stream(middlePanelClass.getDeclaredFields())
        .filter(field -> JComponent.class.isAssignableFrom(field.getType()))
        .collect(Collectors.groupingBy(
            Field::getType,
            Collectors.counting()
        ));
    }

    private Stream<String> provideClassFields() {
        return Arrays.stream(middlePanelClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(final String fieldName) {
        try {
            final Field field = middlePanelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(middlePanel);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }

    /**
     * Counts the occurrences of a specific UI component type within a given JPanel.
     * @param panel The JPanel in which to count the occurrences of the specified component type.
     * @param uiComponent The class of the UI component to count.
     * @return The number of components in the panel that are instances of the specified class.
     */
    private long provideAddedComponentsCount(final JPanel panel, final Class<?> uiComponent) {
        return  Arrays.stream(panel.getComponents())
        .filter(component ->  uiComponent.isInstance(component))
        .count();
    }

    private void invokeRobotKeyPress(final int key) throws AWTException {
        final Robot robot = new Robot();
        final int initialWaitTime = 250;

        new Thread(() -> {
            robot.delay(initialWaitTime);
            robot.keyPress(key);
            robot.keyRelease(key);
        }).start();
    }

    private void assertMethodAddsListener(final String buttonName, final Consumer<ActionListener> addListenerMethod) {
        final JButton clearBtn = (JButton) getComponent(buttonName);
        final int initialListeners = clearBtn.getActionListeners().length;

        try {
            SwingUtilities.invokeAndWait(() -> addListenerMethod.accept(e -> { }));
        } catch (InvocationTargetException | InterruptedException e) {
            throw new IllegalStateException("Failed to add listener to button: " + buttonName, e);
        }

        final int currentListeners = clearBtn.getActionListeners().length;

        assertEquals(initialListeners + 1, currentListeners);
    }
}


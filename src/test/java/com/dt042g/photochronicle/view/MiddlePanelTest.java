package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.dt042g.photochronicle.controller.ChronicleController;
import com.dt042g.photochronicle.support.AppConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    /**
     * Validates that all instance variables are instantiated.
     * @param fieldName
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldHaveInitializedFields(final String fieldName) {
        assertNotNull(getFieldValue(fieldName));
    }

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
            ((JPanel) getFieldValue("labelAndClearBtnWrapper")).getLayout().getClass()
        );
    }

    /**
     * Verifies that the wrapper panel has a JLabel.
     */
    @Test
    void shouldPassIfAJLabelIsAddedToTheWrapperPanel() {
        assertEquals(1, provideAddedComponentsCount(
            (JPanel) getFieldValue("labelAndClearBtnWrapper"),
                JLabel.class
        ));
    }

    /**
     * Verifies that the wrapper panel has a JButton.
     */
    @Test
    void shouldPassIfAJButtonIsAddedToTheWrapperPanel() {
        assertEquals(1, provideAddedComponentsCount(
            (JPanel) getFieldValue("labelAndClearBtnWrapper"),
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
            ((JFileChooser) getFieldValue("fileChooser")).getFileSelectionMode()
        );
    }

    /**
     * Ensure that a selected folder from the JFileChooser dialog updates the path label.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws NoSuchFieldException if fields requested do not exist.
     * @throws IllegalAccessException if fields cannot be accessed.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfSelectedFolderChangesFolderLabel()
            throws AWTException, NoSuchFieldException, IllegalAccessException,
            InterruptedException, InvocationTargetException {
        final ChronicleController controller = new ChronicleController();
        SwingUtilities.invokeAndWait(controller::initializeListeners);

        final JLabel pathLabel = (JLabel) getComponent(controller, "pathLabel");
        final JButton addAndSortBtn = (JButton) getComponent(controller, "addAndSortBtn");
        final JFileChooser fileChooser = (JFileChooser) getComponent(controller, "fileChooser");

        invokeRobotKeyPress(KeyEvent.VK_ENTER);
        addAndSortBtn.doClick();

        assertEquals(fileChooser.getSelectedFile().getAbsolutePath(), pathLabel.getText());
    }

    /**
     * Ensure that cancelling a selection of folder does not modify the path label.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws NoSuchFieldException if fields requested do not exist.
     * @throws IllegalAccessException if fields cannot be accessed.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfCancelFolderSelectionDoesNotModifyLabel()
            throws AWTException, NoSuchFieldException, IllegalAccessException,
            InterruptedException, InvocationTargetException {
        final ChronicleController controller = new ChronicleController();
        SwingUtilities.invokeAndWait(controller::initializeListeners);

        final JLabel pathLabel = (JLabel) getComponent(controller, "pathLabel");
        final JButton addAndSortBtn = (JButton) getComponent(controller, "addAndSortBtn");
        final String dummyPath = "C:\\Foo\\Bar\\";

        pathLabel.setText(dummyPath);
        invokeRobotKeyPress(KeyEvent.VK_ESCAPE);
        addAndSortBtn.doClick();

        assertEquals(dummyPath, pathLabel.getText());
    }

    /**
     * Ensure that the text of the add/soft button is changed to "Sort Folder" when a folder is added.
     * @throws AWTException if {@link MiddlePanelTest#invokeRobotKeyPress(int)} cannot instantiate {@link Robot}.
     * @throws NoSuchFieldException if fields requested do not exist.
     * @throws IllegalAccessException if fields cannot be accessed.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     */
    @Test
    public void shouldPassIfAddButtonRenamedSortWhenFolderIsAdded()
            throws AWTException, NoSuchFieldException, IllegalAccessException,
            InterruptedException, InvocationTargetException {
        final ChronicleController controller = new ChronicleController();
        SwingUtilities.invokeAndWait(controller::initializeListeners);

        final JButton addAndSortBtn = (JButton) getComponent(controller, "addAndSortBtn");

        invokeRobotKeyPress(KeyEvent.VK_ENTER);
        addAndSortBtn.doClick();

        assertEquals(AppConfig.SORT_FOLDER_BUTTON, addAndSortBtn.getText());
    }

    /**
     * Ensure that pressing the clear button resets the path label.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     * @throws NoSuchFieldException if fields requested do not exist.
     * @throws IllegalAccessException if fields cannot be accessed.
     */
    @Test
    public void shouldPassIfClearButtonResetsThePathLabel()
            throws InterruptedException, InvocationTargetException,
            NoSuchFieldException, IllegalAccessException {
        final ChronicleController controller = new ChronicleController();
        SwingUtilities.invokeAndWait(controller::initializeListeners);

        final JLabel pathLabel = (JLabel) getComponent(controller, "pathLabel");
        final JButton clearBtn = (JButton) getComponent(controller, "clearBtn");

        final String dummy = "C:\\Foo\\Bar\\";

        pathLabel.setText(dummy);
        clearBtn.doClick();

        assertEquals(AppConfig.NO_FOLDER_SELECTED, pathLabel.getText());
    }

    /**
     * Ensure that pressing the clear button resets the add/sort button.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if method inside {@link SwingUtilities#invokeAndWait(Runnable)} throws
     * an exception.
     * @throws NoSuchFieldException if fields requested do not exist.
     * @throws IllegalAccessException if fields cannot be accessed.
     */
    @Test
    public void shouldPassIfClearButtonResetsTheFolderButton()
            throws InterruptedException, InvocationTargetException,
            NoSuchFieldException, IllegalAccessException {
        final ChronicleController controller = new ChronicleController();
        SwingUtilities.invokeAndWait(controller::initializeListeners);

        final JButton addSortBtn = (JButton) getComponent(controller, "addAndSortBtn");
        final JButton clearBtn = (JButton) getComponent(controller, "clearBtn");

        addSortBtn.setText(AppConfig.SORT_FOLDER_BUTTON);
        clearBtn.doClick();

        assertEquals(AppConfig.ADD_FOLDER_BUTTON, addSortBtn.getText());
    }

    /*============================
    * Design Integrity Tests
    ============================*/

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

    private Object getFieldValue(final String fieldName)  {
        try {
            final Field field = middlePanelClass.getDeclaredField(fieldName); // Get the field
            field.setAccessible(true); // Allow access to private/final fields
            return field.get(middlePanel); // Get the object from the field
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
        }
    }

    private void invokeRobotKeyPress(final int key) throws AWTException {
        final Robot robot = new Robot();

        final int initialWaitTime = 1000;
        final int keyPressWaitTime = 10;

        new Thread(() -> {
            robot.delay(initialWaitTime);
            robot.keyPress(key);
            robot.delay(keyPressWaitTime);
            robot.keyRelease(key);
        }).start();
    }

    private Object getComponent(
            final ChronicleController controller, final String component)
            throws NoSuchFieldException, IllegalAccessException {
        final Field field = controller.getMiddlePanel().getClass().getDeclaredField(component);
        field.setAccessible(true);
        return field.get(controller.getMiddlePanel());
    }
}


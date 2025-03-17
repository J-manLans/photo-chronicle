package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.dt042g.photochronicle.controller.ChronicleController;
import com.dt042g.photochronicle.support.AppConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class InfoDialogTest {
    private ChronicleController controller;
    private InfoDialog infoDialog;
    private Class<?> infoDialogClass;
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "gbc", "infoDialogPanel", "infoMessage", "infoCloseBtn"
    ));

    /*=====================
    * Setup
    =====================*/

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            controller = new ChronicleController();
            infoDialog = controller.getInfoDialog();
            infoDialog.setModal(false);
        });
        infoDialogClass = infoDialog.getClass();
    }

    /*============================
    * Design Integrity Tests
    ============================*/

    /**
     * Test to ensure that the class is public, so it can be instantiated from another package.
     */
    @Test
    void shouldPassIfInfoDialogIsPublic() {
        assertTrue(Modifier.isPublic(infoDialogClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    void shouldPassIfInfoDialogIsFinal() {
        assertTrue(Modifier.isFinal(infoDialogClass.getModifiers()));
    }

    /**
     * Test to ensure the class extends JDialog.
     */
    @Test
    void shouldPassIfInfoDialogExtendsJDialog() {
        assertEquals(JDialog.class, infoDialogClass.getSuperclass());
    }

    /**
     * Checks that all actual fields exist among the expected ones.
     * @param fieldName the name of the instance field.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfInfoDialogHasNoExtraFields(final String fieldName) {
        assertTrue(expectedFields.contains(fieldName));
    }

    /**
     * Checks that all expected fields are present among the actual ones.
     * @return A stream of dynamic tests, each testing whether an expected field exists in the InfoDialog's fields.
     */
    @TestFactory
    Stream<DynamicTest> shouldPassIfInfoDialogHasNoMissingFields() {
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
        assertTrue(Modifier.isPrivate(infoDialogClass.getDeclaredField(fieldName).getModifiers()));
    }

    /**
     * Test to ensure that no instance field are null.
     * @param fieldName the name of the instance field.
     * @throws NoSuchFieldException if an instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfNoFieldIsNull(final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        assertNotNull(getComponent(fieldName));
    }

    /*===============================
    * Component Verification Tests
    ===============================*/

    /**
     * Test to ensure that the gbc instance field is an instance of {@link GridBagConstraints}.
     */
    @Test
    void shouldPassIfGbcIsGridBagConstraints() {
        assertEquals(GridBagConstraints.class, getField("gbc").getType());
    }

    /**
     * Test to ensure that the infoDialogPanel instance field is an instance of {@link JPanel}.
     */
    @Test
    void shouldPassIfInfoDialogPanelIsJPanel() {
        assertEquals(JPanel.class, getField("infoDialogPanel").getType());
    }

    /**
     * Test to ensure that the infoDialogPanel has a GridBagLayout.
     */
    @Test
    void shouldHaveGridBagLayout() {
        assertEquals(
            GridBagLayout.class,
            ((JPanel) getComponent("infoDialogPanel")).getLayout().getClass()
        );
    }

    /**
     * Test to ensure that the infoMessage instance field is an instance of {@link JLabel}.
     */
    @Test
    void shouldPassIfInfoMessageIsJLabel() {
        assertEquals(JLabel.class, getField("infoMessage").getType());
    }

    /**
     * Test to ensure that the infoCloseBtn instance field is an instance of {@link JButton}.
     */
    @Test
    void shouldPassIfInfoCloseBtnIsJButton() {
        assertEquals(JButton.class, getField("infoCloseBtn").getType());
    }

    /**
     * Test to ensure the MainFrame is the owner of the dialog.
     * This will aid in ensuring it always will be opened in the centre of the main JFrame.
     */
    @Test
    void shouldPassIfMainFrameIsTheOwnerOfInfoDialog() {
        assertEquals(MainFrame.class, infoDialog.getOwner().getClass());
    }

    /**
     * Test to ensure the InfoDialog is undecorated.
     */
    @Test
    void shouldBeUnDecorated() {
        assertTrue(infoDialog.isUndecorated());
    }

    /**
     * Checks that the dialog have the same dimension as the one in the AppConfig file.
     */
    @Test
    void shouldHaveTheSizeFromAppConfig() {
        assertEquals(AppConfig.DIALOG_DIMENSION, infoDialog.getSize());
    }

    /**
     * Checks that the infoDialogPanel is added to the info dialog.
     */
    @Test
    void shouldPassIfInfoPanelIsAddedToInfoDialog() {
        assertSame(getComponent("infoDialogPanel"), infoDialog.getContentPane().getComponent(0));
    }

    /*======================
    * Unit Tests
    ======================*/

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
        SwingUtilities.invokeAndWait(() -> controller.initializeListeners());

        final BottomPanel bottomPanel = controller.getBottomPanel();
        final Field field = bottomPanel.getClass().getDeclaredField("infoButton");
        field.setAccessible(true);
        final JButton infoButton = (JButton) field.get(bottomPanel);

        infoButton.doClick();

        assertTrue(infoDialog.isVisible());
    }

    /*======================
    * Helper Methods
    ======================*/

    Stream<String> provideClassFields() {
        return Arrays.stream(infoDialogClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(final String fieldName) {
        try {
            final Field field = infoDialogClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(infoDialog);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }
}

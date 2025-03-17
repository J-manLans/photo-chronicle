package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class InfoDialogTest {
    private ChronicleController controller;
    private InfoDialog infoDialog;
    private Class<?> infoDialogClass;
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "gbc","infoDialogPanel","infoMessage","infoCloseBtn"
    ));

    /*=====================
    * Setup
    =====================*/

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            controller = new ChronicleController();
            infoDialog = controller.getInfoDialog();
        });
        infoDialogClass = infoDialog.getClass();
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
     * @return A stream of dynamic tests, each testing whether a field in the InfoDialog is part of the expected fields.
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
        List<String> actualFields = provideClassFields().collect(Collectors.toList());

        return expectedFields.stream()
        .map(field -> DynamicTest.dynamicTest("Check field: " + field, () -> {
            assertTrue(actualFields.contains(field));
        }));
    }

    /**
     * Test to ensure that all fields have private access modifier.
     * @throws NoSuchFieldException if an instance field is not present.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfAllInstanceFieldsArePrivate(final String fieldName) throws NoSuchFieldException {
        assertTrue(Modifier.isPrivate(infoDialogClass.getDeclaredField(fieldName).getModifiers()));
    }

    /**
     * Test to ensure that no instance field are null.
     * @throws NoSuchFieldException if an instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfNoFieldIsNull(final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field field = infoDialogClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        assertNotNull(field.get(infoDialog));
    }

    /*======================
    * Helper Methods
    ======================*/

    Stream<String> provideClassFields() {
        return Arrays.stream(infoDialogClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(String fieldName) {
        try {
            Field field = infoDialogClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(String fieldName) {
        try {
            return getField(fieldName).get(infoDialog);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }
}

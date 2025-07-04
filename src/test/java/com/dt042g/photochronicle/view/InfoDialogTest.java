package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for {@link InfoDialog}, ensuring design integrity, component verification,
 * and event handling correctness.
 * @author Joel Lansgren, Daniel Berg
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class InfoDialogTest {
    private InfoDialog infoDialog;
    private Class<?> infoDialogClass;
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "gbc", "infoMessage", "infoCloseBtn"
    ));

    /*=====================
    * Setup
    =====================*/

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            infoDialog = new InfoDialog(new MainFrame(new TopPanel(), new MiddlePanel(), new BottomPanel()));
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

    /**
     * Ensure that a setMessage, taking a String object as the only parameter, is present.
     */
    @Test
    public void shouldPassIfSetMessageMethodIsPresent() {
        assertDoesNotThrow(() -> InfoDialog.class.getDeclaredMethod("setMessage", String.class));
    }

    /**
     * Ensure that the setMessage method does not return a value.
     * @throws NoSuchMethodException if the setMessage method is not present.
     */
    @Test
    public void shouldPassIfSetMessageReturnsVoid() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("setMessage", String.class);
        assertEquals(void.class, method.getReturnType());
    }

    /**
     * Ensure that the setMessage method is not static.
     * @throws NoSuchMethodException if the setMessage method is not present.
     */
    @Test
    public void shouldPassIfSetMessageIsNotStatic() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("setMessage", String.class);
        assertFalse(Modifier.isStatic(method.getModifiers()));
    }

    /**
     * Ensure that a showDialog method is present.
     */
    @Test
    public void shouldPassIfShowDialogMethodIsPresent() {
        assertDoesNotThrow(() -> InfoDialog.class.getDeclaredMethod("showDialog"));
    }

    /**
     * Ensure that the showDialog method does not return a value.
     * @throws NoSuchMethodException if the showDialog method is not present.
     */
    @Test
    public void shouldPassIfShowDialogReturnsVoid() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("showDialog");
        assertEquals(void.class, method.getReturnType());
    }

    /**
     * Ensure that the showDialog method is not static.
     * @throws NoSuchMethodException if the showDialog method is not present.
     */
    @Test
    public void shouldPassIfShowDialogIsNotStatic() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("showDialog");
        assertFalse(Modifier.isStatic(method.getModifiers()));
    }

    /**
     * Ensure that a hideDialog method is present.
     */
    @Test
    public void shouldPassIfHideDialogMethodIsPresent() {
        assertDoesNotThrow(() -> InfoDialog.class.getDeclaredMethod("hideDialog"));
    }

    /**
     * Ensure that the hideDialog method does not return a value.
     * @throws NoSuchMethodException if the hideDialog method is not present.
     */
    @Test
    public void shouldPassIfHideDialogReturnsVoid() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("hideDialog");
        assertEquals(void.class, method.getReturnType());
    }

    /**
     * Ensure that the hideDialog method is not static.
     * @throws NoSuchMethodException if the hideDialog method is not present.
     */
    @Test
    public void shouldPassIfHideDialogIsNotStatic() throws NoSuchMethodException {
        final Method method = InfoDialog.class.getDeclaredMethod("hideDialog");
        assertFalse(Modifier.isStatic(method.getModifiers()));
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
     * Test to ensure that the infoDialog has a GridBagLayout.
     */
    @Test
    void shouldHaveGridBagLayout() {
        assertEquals(GridBagLayout.class, infoDialog.getContentPane().getLayout().getClass());
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
     * Checks that the infoMessage label is added to the info dialog.
     */
    @Test
    void shouldPassIfInfoDialogContainsInfoMessage() {
        assertSame(getComponent("infoMessage"), (JLabel) getComponentFromInfoDialog(JLabel.class));
    }

    /**
     * Checks that the infoCloseBtn is added to the info dialog.
     */
    @Test
    void shouldPassIfInfoDialogContainsInfoCloseBtn() {
        assertSame(getComponent("infoCloseBtn"), (JButton) getComponentFromInfoDialog(JButton.class));
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Ensure that the setMessage method sets the message to the info label component.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    public void shouldPassIfSetMessageUpdatesTheLabel() throws InterruptedException, InvocationTargetException {
        final String testMessage = "This is a test message";
        final JLabel label = (JLabel) getComponent("infoMessage");

        SwingUtilities.invokeAndWait(() -> infoDialog.setMessage(testMessage));

        assertEquals(testMessage, label.getText());
    }

    /**
     * Ensure that the showDialog method shows the dialog.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    public void shouldPassIfShowDialogDisplaysDialog() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> infoDialog.showDialog());
        assertTrue(infoDialog.isVisible());
    }

    /**
     * Ensure that the hideDialog method hides the dialog.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    public void shouldPassIfHideDialogHidesDialog() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            infoDialog.showDialog();
            infoDialog.hideDialog();
        });

        assertFalse(infoDialog.isVisible());
    }

    /**
     * Ensure that the hideDialog method reset the info label.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    public void shouldPassIfHideDialogResetsInfoLabel() throws InterruptedException, InvocationTargetException {
        final String testMessage = "This is a test message.";
        final JLabel label = (JLabel) getComponent("infoMessage");

        SwingUtilities.invokeAndWait(() -> {
            infoDialog.setMessage(testMessage);
            infoDialog.showDialog();
            infoDialog.hideDialog();
        });

        assertEquals(AppConfig.HTML_INFO_LABEL, label.getText());
    }

    /**
     * Validates that the addInfoCloseBtnListener adds a listener to the close button.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    void shouldAttachListenerViaAddInfoCloseBtnListener() throws InvocationTargetException, InterruptedException {
        final JButton closeBtn = (JButton) getComponent("infoCloseBtn");
        final int initialListeners = closeBtn.getActionListeners().length;

        SwingUtilities.invokeAndWait(() -> infoDialog.addInfoCloseBtnListener(e -> { }));

        final int currentListeners = closeBtn.getActionListeners().length;

        assertEquals(initialListeners + 1, currentListeners);
    }

    /*======================
    * Helper Methods
    ======================*/

    private Stream<String> provideClassFields() {
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

    private Component getComponentFromInfoDialog(final Class<?> clazz) {
        return Arrays.stream(infoDialog.getContentPane().getComponents())
        .filter(component -> component.getClass() == clazz)
        .findFirst()
        .orElse(null);
    }
}

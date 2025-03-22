package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.FlowLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for the {@link BottomPanel} class in the {@link com.dt042g.photochronicle.view} package.
 * These tests will verify the integrity of the {@link BottomPanel} class.
 *
 * @author Daniel Berg
 */
public class BottomPanelTest {
    private BottomPanel bottomPanel;
    private final Class<?> bottomPanelClass;

    /**
     * Constructor of the test class, creating a new instance of the {@link BottomPanel} class.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    public BottomPanelTest() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> bottomPanel = new BottomPanel());
        bottomPanelClass = bottomPanel.getClass();
    }

    /*==========================
    * Design Integrity Tests
    ==========================*/

    /**
     * Test to ensure that the class has been marked as public, ensuring classes from other packages can create it.
     */
    @Test
    public void shouldPassIfClassIsPublic() {
        assertTrue(Modifier.isPublic(bottomPanelClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    public void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(bottomPanelClass.getModifiers()));
    }

    /**
     * Test to ensure that the infoButton field has private access modifier.
     */
    @Test
    public void shouldPassIfInfoButtonIsPrivate() {
        assertTrue(Modifier.isPrivate(getField("infoButton").getModifiers()));
    }

    /**
     * Test to ensure that the infoButton instance field is final.
     */
    @Test
    public void shouldPassIfInfoButtonIsFinal() {
        assertTrue(Modifier.isFinal(getField("infoButton").getModifiers()));
    }

    /**
     * Test to ensure that the bottom panel has an instance field called infoButton.
     */
    @Test
    public void shouldPassIfPanelContainsInfoButtonInstanceField() {
        assertDoesNotThrow(() -> getField("infoButton"));
    }

    /**
     * Test to ensure that the infoButton instance field is not null.
     */
    @Test
    public void shouldPassIfInfoButtonIsNotNull() {
        assertNotNull(getComponent("infoButton"));
    }

    /*===============================
    * Component Verification Tests
    ===============================*/

    /**
     * Test to ensure that the panel has a {@link FlowLayout} as its layout.
     */
    @Test
    public void shouldPassIfPanelHasFlowLayout() {
        assertEquals(FlowLayout.class, bottomPanel.getLayout().getClass());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has right alignment.
     */
    @Test
    public void shouldPassIfFlowLayoutHasRightAlignment() {
        final FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
        assertEquals(FlowLayout.RIGHT, flowLayout.getAlignment());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a horizontal gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectHorizontalGap() {
        final FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
        assertEquals(AppConfig.FLOW_GAP, flowLayout.getHgap());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a vertical gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectVerticalGap() {
        final FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
        assertEquals(AppConfig.FLOW_GAP, flowLayout.getVgap());
    }

    /**
     * Test to ensure that the infoButton instance field is an instance of {@link JButton}.
     */
    @Test
    public void shouldPassIfInfoButtonIsJButton() {
        assertEquals(JButton.class, getField("infoButton").getType());
    }

    /**
     * Test to ensure that the infoButton instance field has the correct text.
     */
    @Test
    public void shouldPassIfInfoButtonTextIsCorrect() {
        assertEquals("Info", ((JButton) getComponent("infoButton")).getText());
    }

    /**
     * Test to ensure that the infoButton instance field have been added to the panel.
     */
    @Test
    public void shouldPassIfInfoButtonIsAddedToPanel() {
        assertEquals(JButton.class, ((JButton) getComponent("infoButton")).getClass());
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Validates that the addInfoButtonListener adds a listener to the infoButton.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    @Test
    void shouldAttachListenerViaAddInfoButtonListener() throws InvocationTargetException, InterruptedException {
        final JButton infoButton = (JButton) getComponent("infoButton");
        final int initialListeners = infoButton.getActionListeners().length;

        SwingUtilities.invokeAndWait(() -> bottomPanel.addInfoButtonListener(e -> { }));

        final int currentListeners = infoButton.getActionListeners().length;

        assertEquals(initialListeners + 1, currentListeners);
    }

    /*======================
    * Helper Methods
    ======================*/

    private Field getField(final String fieldName) {
        try {
            final Field field = bottomPanelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(bottomPanel);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }
}

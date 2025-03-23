package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.FlowLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for the {@link TopPanel} class in the {@link com.dt042g.photochronicle.view} package.
 * These tests will verify the integrity of the {@link TopPanel} class.
 *
 * @author Daniel Berg
 */
public class TopPanelTest {
    private TopPanel topPanel;
    private final Class<?> topPanelClass;

    /**
     * Constructor of the test class, creating a new instance of the {@link TopPanel} class.
     * @throws InterruptedException if {@link SwingUtilities#invokeAndWait(Runnable)} is interrupted.
     * @throws InvocationTargetException if invocations in {@link SwingUtilities#invokeAndWait(Runnable)} throws an
     * exception.
     */
    public TopPanelTest() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> topPanel = new TopPanel());
        topPanelClass = topPanel.getClass();
    }

    /*==========================
    * Design Integrity Tests
    ==========================*/

    /**
     * Test to ensure that the class has public access modifier.
     */
    @Test
    public void shouldPassIfClassIsPublic() {
        assertTrue(Modifier.isPublic(topPanelClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    public void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(topPanelClass.getModifiers()));
    }

    /**
     * Test to ensure that the top panel has an instance field called title.
     */
    @Test
    public void shouldPassIfPanelContainsTitleLabel() {
        assertDoesNotThrow(() -> getField("title"));
    }

    /**
     * Test to ensure that the title instance field has private access modifier.
     */
    @Test
    public void shouldPassIfLabelIsPrivate() {
        assertTrue(Modifier.isPrivate(getField("title").getModifiers()));
    }

    /**
     * Test to ensure that the title instance field has final access modifier.
     */
    @Test
    public void shouldPassIfLabelIsFinal() {
        assertTrue(Modifier.isFinal(getField("title").getModifiers()));
    }

    /**
     * Test to ensure that the title instance field is not static.
     */
    @Test
    public void shouldPassIfLabelIsNotStatic() {
        assertFalse(Modifier.isStatic(getField("title").getModifiers()));
    }

    /**
     * Test to ensure that the title instance field is not null.
     */
    @Test
    public void shouldPassIfLabelIsNotNull() {
        assertNotNull(getComponent("title"));
    }

    /*===============================
    * Component Verification Tests
    ===============================*/

    /**
     * Test to ensure that the panel has a {@link FlowLayout} as its layout.
     */
    @Test
    public void shouldPassIfPanelHasFlowLayout() {
        assertEquals(FlowLayout.class, topPanel.getLayout().getClass());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has left alignment.
     */
    @Test
    public void shouldPassIfFlowLayoutHasLeftAlignment() {
        assertEquals(FlowLayout.LEFT, ((FlowLayout) topPanel.getLayout()).getAlignment());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a horizontal gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectHorizontalGap() {
        assertEquals(AppConfig.FLOW_GAP, ((FlowLayout) topPanel.getLayout()).getHgap());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a vertical gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectVerticalGap() {
        assertEquals(AppConfig.FLOW_GAP, ((FlowLayout) topPanel.getLayout()).getVgap());
    }

    /**
     * Test to ensure that the title instance field is an instance of {@link JLabel}.
     */
    @Test
    public void shouldPassIfLabelIsJLabel() {
        assertEquals(JLabel.class, getComponent("title").getClass());
    }

    /**
     * Test to ensure that the title instance field has the correct text.
     */
    @Test
    public void shouldPassIfLabelTextIsCorrect() {
        assertEquals(AppConfig.APP_NAME, ((JLabel) getComponent("title")).getText());
    }

    /**
     * Test to ensure that the title instance field has been added to the panel.
     */
    @Test
    public void shouldPassIfLabelIsAddedToPanel() {
        assertEquals(JLabel.class, topPanel.getComponent(0).getClass());
    }

    /**
     * Test to ensure that the title instance field has correct font size.
     */
    @Test
    public void shouldPassIfLabelHasCorrectFontSize() {
        assertEquals(AppConfig.TEXT_SIZE_TITLE, ((JLabel) getComponent("title")).getFont().getSize());
    }

    /*======================
    * Helper Methods
    ======================*/

    private Field getField(final String fieldName) {
        try {
            final Field field = topPanelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(topPanel);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }
}

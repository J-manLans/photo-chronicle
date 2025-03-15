package com.dt042g.photochronicle.view;

import com.dt042g.photochronicle.support.AppConfig;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link BottomPanel} class in the {@link com.dt042g.photochronicle.view} package.
 * These tests will verify the integrity of the {@link BottomPanel} class.
 *
 * @author Daniel Berg
 */
public class BottomPanelTest {
    private final BottomPanel bottomPanel = new BottomPanel();

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    public void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(BottomPanel.class.getModifiers()));
    }

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
     * Test to ensure that the bottom panel has an instance field called infoButton.
     */
    @Test
    public void shouldPassIfPanelContainsInfoButtonInstanceField() {
        assertDoesNotThrow(() -> BottomPanel.class.getDeclaredField("infoButton"));
    }

    /**
     * Test to ensure that the infoButton field has private access modifier.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     */
    @Test
    public void shouldPassIfInfoButtonIsPrivate() throws NoSuchFieldException {
        final Field field = BottomPanel.class.getDeclaredField("infoButton");
        assertTrue(Modifier.isPrivate(field.getModifiers()));
    }

    /**
     * Test to ensure that the infoButton instance field is not null.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfInfoButtonIsNotNull() throws NoSuchFieldException, IllegalAccessException {
        final Field field = BottomPanel.class.getDeclaredField("infoButton");
        field.setAccessible(true);
        assertNotNull(field.get(bottomPanel));
    }

    /**
     * Test to ensure that the infoButton instance field is final.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     */
    @Test
    public void shouldPassIfInfoButtonIsFinal() throws NoSuchFieldException {
        final Field field = BottomPanel.class.getDeclaredField("infoButton");
        assertTrue(Modifier.isFinal(field.getModifiers()));
    }

    /**
     * Test to ensure that the infoButton instance field is an instance of {@link JButton}.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfInfoButtonIsJButton() throws NoSuchFieldException, IllegalAccessException {
        final Field field = BottomPanel.class.getDeclaredField("infoButton");
        field.setAccessible(true);
        assertEquals(JButton.class, field.getType());
    }

    /**
     * Testto ensure that the infoButton instance field has the correct text.
     * @throws NoSuchFieldException if the infoButton instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfInfoButtonTestIsCorrect() throws NoSuchFieldException, IllegalAccessException {
        final Field field = BottomPanel.class.getDeclaredField("infoButton");
        field.setAccessible(true);
        final JButton button = (JButton) field.get(bottomPanel);
        assertEquals("Info", button.getText());
    }

    /**
     * Test to ensure that the infoButton instance field have been added to the panel.
     */
    @Test
    public void shouldPassIfInfoButtonIsAddedToPanel() {
        assertEquals(JButton.class, bottomPanel.getComponent(0).getClass());
    }
}

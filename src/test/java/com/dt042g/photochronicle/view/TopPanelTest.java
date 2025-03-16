package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.FlowLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.JLabel;

import org.junit.jupiter.api.Test;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for the {@link TopPanel} class in the {@link com.dt042g.photochronicle.view} package.
 * These tests will verify the integrity of the {@link TopPanel} class.
 *
 * @author Daniel Berg
 */
public class TopPanelTest {
    final TopPanel topPanel = new TopPanel();

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    public void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(TopPanel.class.getModifiers()));
    }

    /**
     * Test to ensure that the class has public access modifier.
     */
    @Test
    public void shouldPassIfClassIsPublic() {
        assertTrue(Modifier.isPublic(TopPanel.class.getModifiers()));
    }

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
        final FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
        assertEquals(FlowLayout.LEFT, flowLayout.getAlignment());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a horizontal gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectHorizontalGap() {
        final FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
        assertEquals(AppConfig.FLOW_GAP, flowLayout.getHgap());
    }

    /**
     * Test to ensure that the {@link FlowLayout} of the panel has a vertical gap of 15.
     */
    @Test
    public void shouldPassIfFlowLayoutHasCorrectVerticalGap() {
        final FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
        assertEquals(AppConfig.FLOW_GAP, flowLayout.getVgap());
    }

    /**
     * Test to ensure that the top panel has an instance field called title.
     */
    @Test
    public void shouldPassIfPanelContainsTitleLabel() {
        assertDoesNotThrow(() -> TopPanel.class.getDeclaredField("title"));
    }

    /**
     * Test to ensure that the title instance field is not static.
     * @throws NoSuchFieldException if the title instance field is not present.
     */
    @Test
    public void shouldPassIfLabelIsNotStatic() throws NoSuchFieldException {
        final Field field = TopPanel.class.getDeclaredField("title");
        assertFalse(Modifier.isStatic(field.getModifiers()));
    }

    /**
     * Test to ensure that the title instance field has public access modifier.
     * @throws NoSuchFieldException if the title instance field is not present.
     */
    @Test
    public void shouldPassIfLabelIsPrivate() throws NoSuchFieldException {
        final Field field = TopPanel.class.getDeclaredField("title");
        assertTrue(Modifier.isPrivate(field.getModifiers()));
    }

    /**
     * Test to ensure that the title instance field is not null.
     * @throws NoSuchFieldException if the title instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfLabelIsNotNull() throws NoSuchFieldException, IllegalAccessException {
        final Field field = TopPanel.class.getDeclaredField("title");
        field.setAccessible(true);
        assertNotNull(field.get(topPanel));
    }

    /**
     * Test to ensure that the title instance field is an instance of {@link JLabel}.
     * @throws NoSuchFieldException if the title instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfLabelIsJLabel() throws NoSuchFieldException, IllegalAccessException {
        final Field field = TopPanel.class.getDeclaredField("title");
        field.setAccessible(true);
        assertEquals(JLabel.class, field.get(topPanel).getClass());
    }

    /**
     * Test to ensure that the title instance field has the correct text.
     * @throws NoSuchFieldException if the title instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfLabelTextIsCorrect() throws NoSuchFieldException, IllegalAccessException {
        final Field field = TopPanel.class.getDeclaredField("title");
        field.setAccessible(true);
        final JLabel label = (JLabel) field.get(topPanel);
        assertEquals(AppConfig.APP_NAME, label.getText());
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
     * @throws NoSuchFieldException if the title instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @Test
    public void shouldPassIfLabelHasCorrectFontSize() throws NoSuchFieldException, IllegalAccessException {
        final Field field = TopPanel.class.getDeclaredField("title");
        field.setAccessible(true);
        final JLabel label = (JLabel) field.get(topPanel);
        assertEquals(AppConfig.TEXT_SIZE_TITLE, label.getFont().getSize());
    }
}

package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MiddlePanelTest {
    private MiddlePanel middlePanel;
    Class<?> middlePanelClass;

    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            middlePanel = new MiddlePanel();
        });
        middlePanelClass = middlePanel.getClass();
    }

    /**
     * Validates that the MiddlePanel has a GridBagLayout.
     */
    @Test
    void shouldPassIfMiddlePanelHasGridBagLayout() {
        assertEquals(GridBagLayout.class, middlePanel.getLayout().getClass());
    }

    @Test
    void shouldPassIfAPanelIsAddedToTheMiddlePanel() {
        assertEquals(1, provideAddedComponentsCount(middlePanel, JPanel.class));
    }

    @Test
    void shouldPassIfALabelIsAddedToTheWrapperPanel() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(1, provideAddedComponentsCount(provideFieldWrapperPanel(), JLabel.class));
    }

    @Test
    void shouldPassIfAButtonIsAddedToTheWrapperPanel() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(1, provideAddedComponentsCount(provideFieldWrapperPanel(), JButton.class));
    }

    @Test
    void shouldPassIfWrapperPanelHasFlowLayout() throws IllegalAccessException, NoSuchFieldException {
        assertEquals(FlowLayout.class, provideFieldWrapperPanel().getLayout().getClass());
    }

    @Test
    void shouldPassIfAButtonIsAddedToTheMiddlePanel() {
        assertEquals(1, provideAddedComponentsCount(middlePanel, JButton.class));
    }

    /*============================
    * Design Integrity Tests
    ============================*/

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
     * Verifies that MiddlePanel only have one constructor.
     */
    @Test
    void shouldPassIfMiddlePanelHasOneConstructor() {
        assertTrue(1 == middlePanelClass.getDeclaredConstructors().length);
    }

    /**
     * Verifies that the constructor is public.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the constructor is not found in the MiddlePanel class.
     */
    @Test
    void shouldPassIfConstructorModifierIsPublic() throws NoSuchMethodException, SecurityException  {
        assertTrue(Modifier.isPublic(middlePanelClass.getDeclaredConstructor().getModifiers()));
    }

    /**
     * Validates that all instance variables are instantiated.
     */
    @Test
    void shouldPassIfAllFieldsAreInstantiated() {
        Field[] fields = middlePanelClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(middlePanel);
                assertNotNull(fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Fails the test if one exception occur.
                fail("\nError accessing field " + field.getName() + ":\n" + e.getClass() + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Validates that the class contains two buttons.
     */
    @Test
    void shouldPassIfClassContainsTwoButtons() {
        Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(2, componentCounts.get(JButton.class));
    }

    /**
     * Validates that the class contains one label.
     */
    @Test
    void shouldPassIfClassContainsOneLabel() {
        Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JLabel.class));
    }

    /**
     * Validates that the class contains one panel.
     */
    @Test
    void shouldPassIfClassContainsOnePanel() {
        Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JPanel.class));
    }

    /**
     * Validates that the class contains one file chooser.
     */
    @Test
    void shouldPassIfClassContainsOneFileChooser() {
        Map<Class<?>, Long> componentCounts = provideMapOfAllUIComponents();
        assertEquals(1, componentCounts.get(JFileChooser.class));
    }

    /*============================
    * Helper methods
    ============================*/

    /**
     * Creates a stream from the fields array and filters out the variables that extend
     * {@link JComponent}. The {@code groupingBy} method that returns a map is then used
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

    private Long provideAddedComponentsCount(JPanel panel, Class<?> uiComponent) {
        return  Arrays.stream(panel.getComponents())
        .filter(component ->  uiComponent.isInstance(component))
        .count();
    }

    private JPanel provideFieldWrapperPanel() throws IllegalAccessException, NoSuchFieldException {
        Field field = middlePanelClass.getDeclaredField("labelAndClearBtnWrapper");
        field.setAccessible(true);
        return (JPanel) field.get(middlePanel);
    }
}

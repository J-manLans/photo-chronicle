package com.dt042g.photochronicle.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.dt042g.photochronicle.controller.ChronicleController;
import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for the {@link MainFrame} class in the {@link com.dt042g.photochronicle.view} package.
 *
 * <p>This test class verifies the functionality and design integrity of the {@link MainFrame} class,
 * ensuring that it behaves as expected in various scenarios.</p>
 *
 * <p>The tests include checking the layout, size, title, behavior on close, positioning, and panels of the
 * main frame. Additionally, the design integrity is verified by checking the accessibility of the
 * constructor and class modifiers.</p>
 *
 * @author Joel Lansgren
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainFrameTest {
    private ChronicleController controller;
    private MainFrame mainFrame;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    private Class<?> mainFrameClass;

    /*============================
    * Setup
    ============================*/

    /**
     * Sets up the test environment before all tests are run.
     *
     * <p>This method is executed once before any of the tests in the class.
     * It initializes the {@link ChronicleController} and retrieves the necessary components
     * for the tests. The {@link MainFrame} and associated panels (top, middle, bottom) are
     * also initialized and stored for use in the test methods.</p>
     *
     * <p>The method ensures that all Swing components are initialized on the Event Dispatch Thread
     * by using {@link SwingUtilities#invokeAndWait(Runnable)}. This is critical to avoid issues
     * related to thread safety in Swing testing.</p>
     *
     * @throws InvocationTargetException if the code invoked by {@link SwingUtilities#invokeAndWait(Runnable)}
     * throws an exception
     * @throws InterruptedException if the current thread is interrupted while waiting for the event dispatch thread
     */
    @BeforeAll
    private void setup() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            controller = new ChronicleController();
            mainFrame = controller.getMainFrame();
            topPanel = controller.getTopPanel();
            middlePanel = controller.getMiddlePanel();
            bottomPanel = controller.getBottomPanel();
        });
        mainFrameClass = mainFrame.getClass();
    }

    /*============================
    * Design Integrity Tests
    ============================*/

    /**
     * Verifies that the MainFrame class is final using reflection.
     */
    @Test
    void shouldPassIfClassIsFinal() {
        assertTrue(Modifier.isFinal(mainFrameClass.getModifiers()));
    }

    /**
     * Verifies that the MainFrame class is public using reflection.
     */
    @Test
    void shouldPassIfClassIsPublic() {
        assertTrue(Modifier.isPublic(mainFrameClass.getModifiers()));
    }

    /**
     * Verifies that MainFrame only has one constructor.
     */
    @Test
    void shouldPassIfMainFrameOnlyHasOneConstructor() {
        assertTrue(mainFrameClass.getDeclaredConstructors().length == 1);
    }

    /**
     * Verifies that MainFrame's constructor is public.
     * This ensures that the class can be instantiated.
     */
    @Test
    void shouldPassIfConstructorModifierIsPublic() {
        assertTrue(Modifier.isPublic(mainFrameClass.getDeclaredConstructors()[0].getModifiers()));
    }

    /**
     * Checks that the constructors parameters equals the components of the MainFrame.
     * While this is no guarantee that the correct panels have been added to the frame
     * it's a good sign something is of if that is not the case.
     */
    @Test
    void shouldPassIfConstructorParametersEqualsComponentCount() {
        int addedComponents = mainFrame.getContentPane().getComponentCount();
        assertEquals(mainFrameClass.getDeclaredConstructors()[0].getParameterCount(), addedComponents);
    }

    /*===============================
    * Component Verification Tests
    ===============================*/

    /**
     * Validates that the MainFrame has a BorderLayout.
     */
    @Test
    public void shouldPassIfMainFrameHasBorderLayout() {
        assertEquals(BorderLayout.class, mainFrame.getLayout().getClass());
    }

    /**
     * Validates that the MainFrame's Dimension equals the one in the config file.
     */
    @Test
    void shouldPassIfSizeIsCorrect() {
        assertEquals(AppConfig.APP_DIMENSION, mainFrame.getPreferredSize());
    }

    /**
     * Validates that the MainFrame can't be resizable.
     */
    @Test
    void shouldPassIfResizableIsDisabled() {
        assertEquals(false, mainFrame.isResizable());
    }

    /**
     * Validates that the MainFrame's title equals the one in the config file.
     */
    @Test
    void shouldPassIfTitleIsCorrect() {
        assertEquals(AppConfig.APP_NAME, mainFrame.getTitle());
    }

    /**
     * Validates that the application exits when the MainFrame is closed.
     */
    @Test
    void shouldPassIfAppClosesOnExit() {
        assertEquals(JFrame.EXIT_ON_CLOSE, mainFrame.getDefaultCloseOperation());
    }

    /**
     * Checks that the topPanel have been added to the correct position.
     */
    @Test
    void shouldPassIfTopPanelIsInRightPosition() {
        BorderLayout borderLayout = (BorderLayout) mainFrame.getContentPane().getLayout();
        assertSame(topPanel, borderLayout.getLayoutComponent(BorderLayout.NORTH));
    }

    /**
     * Checks that the middlePanel have been added to the correct position.
     */
    @Test
    void shouldPassIfMiddlePanelIsInRightPosition() {
        BorderLayout borderLayout = (BorderLayout) mainFrame.getContentPane().getLayout();
        assertSame(middlePanel, borderLayout.getLayoutComponent(BorderLayout.CENTER));
    }

    /**
     * Checks that the bottomPanel have been added to the correct position.
     */
    @Test
    void shouldPassIfBottomPanelIsInRightPosition() {
        BorderLayout borderLayout = (BorderLayout) mainFrame.getContentPane().getLayout();
        assertSame(bottomPanel, borderLayout.getLayoutComponent(BorderLayout.SOUTH));
    }

    /*======================
    * Integration Test
    ======================*/

    /**
     * Validates that the MainFrame is centered in the screen upon start.
     * @throws InvocationTargetException if the code invoked by {@link SwingUtilities#invokeAndWait(Runnable)}
     * throws an exception
     * @throws InterruptedException if the current thread is interrupted while waiting for the event dispatch thread
     */
    @Test
    void shouldPassIfAppIsCenteredOnStart() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            // Collects the top left position of the MainFrame
            mainFrame.setVisible(true);
            Point actualTopLeftPosition = mainFrame.getLocationOnScreen();
            mainFrame.setVisible(false);

            // Gets the screen size with the taskbar taken out of the equation.
            Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

            double screenWidth =  screenBounds.getWidth();
            double screenHeight =  screenBounds.getHeight();

            int frameWidth = mainFrame.getWidth();
            int frameHeight = mainFrame.getHeight();

            // Creates the expected point by rounding to the nearest integer to
            // mimic the behavior of getLocationOnScreen.
            Point expectedTopLeftPoint = new Point(
                (int) Math.round((screenWidth - frameWidth)) / 2, (int) Math.round((screenHeight - frameHeight) / 2)
            );

            assertEquals(expectedTopLeftPoint, actualTopLeftPosition);
        });
    }
}

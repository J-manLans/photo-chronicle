package com.dt042g.photochronicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Unit tests which use reflection for the Main class to verify its integrity to expected design principles.
 *
 * <p>These tests focus on checking the visibility and modifiers of the Main class and its methods,
 * ensuring that the class is properly structured according to best practices for utility classes
 * with a proper entry point in Java.</p>
 *
 * @author Joel Lansgren
 */
public class MainTest {

    /*============================
    * Integrity Tests
    ============================*/

    /**
     * Verifies that the Main class is public using reflection.
     * This ensures that the class has the correct visibility.
     * @throws ClassNotFoundException if the class cannot be found via reflection.
     */
    @Test
    void shouldReturnTrueIfClassModifierIsPublic() throws ClassNotFoundException {
        assertTrue(Modifier.isPublic(Main.class.getModifiers()));
    }

    /**
     * Verifies that the Main class is final using reflection.
     * This ensures that the class cannot be subclassed.
     * @throws ClassNotFoundException if the class cannot be found via reflection.
     */
    @Test
    void shouldReturnTrueIfClassModifierIsFinal() throws ClassNotFoundException {
        assertTrue(Modifier.isFinal(Main.class.getModifiers()));
    }

    /**
     * Verifies that Main's constructor is private.
     * This ensures that the utility class cant be instantiated.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the main method is not found in the Main class.
     */
    @Test
    void shouldReturnTrueIfConstructorModifierIsPrivate() throws NoSuchMethodException, SecurityException {
        assertTrue(Modifier.isPrivate(Main.class.getDeclaredConstructor().getModifiers()));
    }

    /**
     * Verifies that the Main class main method is public.
     * This ensures that the main method has the correct visibility for the JVM to invoke it as the entry point.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the main method is not found in the Main class.
     */
    @Test
    void shouldReturnTrueIfMainMethodIsPublic() throws NoSuchMethodException, SecurityException {
        assertTrue(Modifier.isPublic(Main.class.getMethod(
            "main", String[].class
        ).getModifiers()));
    }

    /**
     * Verifies that the Main class main method is static.
     * This ensures that the main method can be invoked without needing to instantiate the Main class,
     * as required by the Java runtime environment when starting a Java application.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the main method is not found in the Main class.
     */
    @Test
    void shouldReturnTrueIfMainMethodIsStatic() throws NoSuchMethodException, SecurityException {
        assertTrue(Modifier.isStatic(Main.class.getMethod(
            "main", String[].class
        ).getModifiers()));
    }

    /**
     * Verifies that the Main class main method don't have a return type.
     * This ensures that the main method is correctly defined with a void return type, as required
     * by the Java runtime environment for the entry point method of a Java application.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the main method is not found in the Main class.
     */
    @Test
    void shouldReturnTrueIfMainMethodReturnsVoid() throws NoSuchMethodException, SecurityException {
        assertEquals(void.class, Main.class.getMethod(
            "main", String[].class
        ).getReturnType());
    }
}

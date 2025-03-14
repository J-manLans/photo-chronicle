package com.dt042g.photochronicle.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests which use reflection for the AppConfig class to verify its integrity to expected design principles.
 *
 * <p>These tests focus on checking the visibility and modifiers of the AppConfig class its constructor and variables,
 * ensuring that the class is properly structured according to best practices for utility classes.</p>
 *
 * @author Joel Lansgren
 */
public class AppConfigTest {

    /*============================
    * Integrity Tests
    ============================*/

    /**
     * Verifies that the AppConfig class is public using reflection.
     * This ensures that the class has the correct visibility.
     * @throws ClassNotFoundException if the class cannot be found via reflection.
     */
    @Test
    void shouldReturnTrueIfClassModifierIsPublic() throws ClassNotFoundException {
        assertTrue(Modifier.isPublic(AppConfig.class.getModifiers()));
    }

    /**
     * Verifies that the AppConfig class is final using reflection.
     * This ensures that the class cannot be subclassed.
     * @throws ClassNotFoundException if the class cannot be found via reflection.
     */
    @Test
    void shouldReturnTrueIfClassModifierIsFinal() throws ClassNotFoundException {
        assertTrue(Modifier.isFinal(AppConfig.class.getModifiers()));
    }

    /**
     * Verifies that AppConfig's constructor is private.
     * This ensures that the utility class can't be instantiated.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the constructor is not found in the AppConfig class.
     */
    @Test
    void shouldReturnTrueIfConstructorModifierIsPrivate() throws NoSuchMethodException, SecurityException {
        assertTrue(Modifier.isPrivate(AppConfig.class.getDeclaredConstructor().getModifiers()));
    }

    /**
     * Verifies that AppConfig's constructor throws an IllegalStateException when attempting to instantiate it.
     * This also ensures that the utility class can't be instantiated.
     * @throws SecurityException if the security manager blocks access to the method.
     * @throws NoSuchMethodException if the constructor is not found in the AppConfig class.
     */
    @Test
    void shouldTrowIllegalStateExceptionWhenAttemptingToInstantiate() throws NoSuchMethodException, SecurityException {
        Constructor<AppConfig> constructor = AppConfig.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
        } catch (Exception e) {
            // InvocationTargetException is thrown when the constructor throws an exception.
            // So we need to access the 'cause' of the InvocationTargetException to get the
            // original exception (IllegalStateException).
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * Verifies that all fields in the {@link AppConfig} class are final.
     * @param fieldName the name of the field being checked
     * @throws SecurityException if there is a security manager that prevents accessing the field
     * @throws NoSuchFieldException if the field with the given name does not exist in the {@link AppConfig} class.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldReturnTrueForPrivateFields(final String fieldName) throws NoSuchFieldException, SecurityException  {
        assertTrue(
            Modifier.isFinal(AppConfig.class.getDeclaredField(fieldName).getModifiers()),
            "Attribute 'MathLexer::" + fieldName + "' needs to be final."
        );
    }

    /**
     * Verifies that all fields in the {@link AppConfig} class are static.
     * @param fieldName the name of the field being checked
     * @throws SecurityException if there is a security manager that prevents accessing the field
     * @throws NoSuchFieldException if the field with the given name does not exist in the {@link AppConfig} class.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldReturnTrueForStaticFields(final String fieldName) throws NoSuchFieldException, SecurityException  {
        assertTrue(
            Modifier.isStatic(AppConfig.class.getDeclaredField(fieldName).getModifiers()),
            "Attribute 'MathLexer::" + fieldName + "' needs to be final."
        );
    }

    private static Stream<String> provideClassFields() {
        return Arrays.stream(AppConfig.class.getDeclaredFields())
        .map(Field::getName);
    }
}

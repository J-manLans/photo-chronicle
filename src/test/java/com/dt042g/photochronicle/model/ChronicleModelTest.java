package com.dt042g.photochronicle.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.dt042g.photochronicle.support.AppConfig;

/**
 * Unit tests for {@link ChronicleModel}, ensuring design integrity and correct functionality.
 * @author Joel Lansgren
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChronicleModelTest {
    private ChronicleModel model = new ChronicleModel();
    private Class<?> modelClass = model.getClass();
    private final List<String> expectedFields = new ArrayList<>(List.of(
        "path"
    ));

    /*========================
    * Design Integrity Tests
    ========================*/

    /**
     * Test to ensure that the class is public, so it can be instantiated from another package.
     */
    @Test
    void shouldPassIfChronicleModelIsPublic() {
        assertTrue(Modifier.isPublic(modelClass.getModifiers()));
    }

    /**
     * Test to ensure that the class has been marked as final, preventing it to be subclassed.
     */
    @Test
    void shouldPassIfChronicleModelIsFinal() {
        assertTrue(Modifier.isFinal(modelClass.getModifiers()));
    }

    /**
     * Checks that all actual fields exist among the expected ones.
     * @param fieldName the name of the instance field.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfChronicleModelHasNoExtraFields(final String fieldName) {
        assertTrue(expectedFields.contains(fieldName));
    }

    /**
     * Checks that all expected fields are present among the actual ones.
     * @return A stream of dynamic tests, each testing whether an expected field exists in the ChronicleModel's fields.
     */
    @TestFactory
    Stream<DynamicTest> shouldPassIfChronicleModelHasNoMissingFields() {
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
        assertTrue(Modifier.isPrivate(modelClass.getDeclaredField(fieldName).getModifiers()));
    }

    /**
     * Test to ensure that no final instance fields are null.
     * @param fieldName the name of the instance field.
     * @throws NoSuchFieldException if an instance field is not present.
     * @throws IllegalAccessException if the {@link Field} access is illegal.
     */
    @ParameterizedTest
    @MethodSource("provideClassFields")
    void shouldPassIfNoFieldIsNull(final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (Modifier.isFinal(getField(fieldName).getModifiers())) {
            assertNotNull(getComponent(fieldName));
        }
    }

    /*======================
    * Unit Tests
    ======================*/

    /**
     * Tests that the Path is successfully set through the setPath method.
     */
    @Test
    void shouldHaveTheSetPath() {
        model.setPath(AppConfig.TEST_FOLDER_PATH);
        assertEquals(AppConfig.TEST_FOLDER_PATH, getComponent("path").toString());
    }

    /**
     * Tests that the sortFolder method have a Consumer<String> parameter and nothing else.
     * @throws NoSuchMethodException if the sortFolder method is not present.
     */
    @Test
    void shouldHaveAConsumerStringAsParameter() throws NoSuchMethodException {
        // Confirms the method have one parameter and that it's a consumer
        Method method = modelClass.getDeclaredMethod("sortFolder", Consumer.class);
        Parameter[] parameters = method.getParameters();
        Type parameterType = null;
        String parameterTypeName = null;

        for (Parameter parameter : parameters) {
            parameterType = parameter.getParameterizedType();
            parameterTypeName = parameterType.getTypeName();
        }

        // And here we check if the parameters type name contains "String"
        assertTrue(parameterTypeName.contains("String"));
    }

    /*======================
    * Helper Methods
    ======================*/

    private Stream<String> provideClassFields() {
        return Arrays.stream(modelClass.getDeclaredFields())
        .map(Field::getName);
    }

    private Field getField(final String fieldName) {
        try {
            final Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " don't exist", e);
        }
    }

    private Object getComponent(final String fieldName) {
        try {
            return getField(fieldName).get(model);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field: " + fieldName, e);
        }
    }
}

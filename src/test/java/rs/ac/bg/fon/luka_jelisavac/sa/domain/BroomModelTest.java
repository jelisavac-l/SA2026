package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BroomModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void validate_ValidModel_NoViolations() {
        BroomModel model = new BroomModel(UUID.randomUUID(), "Firebolt", 180.0, 1993);

        Set<ConstraintViolation<BroomModel>> violations = validator.validate(model);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when model name is blank")
    void validate_BlankModelName_HasViolations() {
        BroomModel model = new BroomModel();
        model.setModelName("");
        model.setTopSpeed(100.0);

        Set<ConstraintViolation<BroomModel>> violations = validator.validate(model);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("modelName")));
    }

    @Test
    @DisplayName("Should fail validation when top speed is not positive")
    void validate_NonPositiveSpeed_HasViolations() {
        BroomModel model = new BroomModel();
        model.setModelName("Comet 260");
        model.setTopSpeed(-5.0);

        Set<ConstraintViolation<BroomModel>> violations = validator.validate(model);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("topSpeed")));
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokMethods() {
        UUID id = UUID.randomUUID();
        BroomModel model = new BroomModel();

        model.setId(id);
        model.setModelName("Shooting Star");
        model.setTopSpeed(70.0);
        model.setReleaseYear(1955);

        assertEquals(id, model.getId());
        assertEquals("Shooting Star", model.getModelName());
        assertEquals(70.0, model.getTopSpeed());
        assertEquals(1955, model.getReleaseYear());
    }

    @Test
    @DisplayName("Should verify AllArgsConstructor initializes all fields")
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        BroomModel model = new BroomModel(id, "Silver Arrow", 90.0, 1901);

        assertAll(
            () -> assertEquals(id, model.getId()),
            () -> assertEquals("Silver Arrow", model.getModelName()),
            () -> assertEquals(90.0, model.getTopSpeed()),
            () -> assertEquals(1901, model.getReleaseYear())
        );
    }
}
package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


class BroomstickTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation when all broomstick fields are within valid ranges")
    void validate_ValidBroomstick_NoViolations() {
        Broomstick broomstick = new Broomstick();
        broomstick.setSerialNumber("SN-2026-X");
        broomstick.setPurchasePrice(500.0);
        broomstick.setCurrentCondition(100);

        Set<ConstraintViolation<Broomstick>> violations = validator.validate(broomstick);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when serial number is blank")
    void validate_BlankSerialNumber_HasViolations() {
        Broomstick broomstick = new Broomstick();
        broomstick.setSerialNumber(" ");
        broomstick.setPurchasePrice(100.0);
        broomstick.setCurrentCondition(50);

        Set<ConstraintViolation<Broomstick>> violations = validator.validate(broomstick);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("serialNumber")));
    }

    @Test
    @DisplayName("Should fail validation when purchase price is not positive")
    void validate_NegativePrice_HasViolations() {
        Broomstick broomstick = new Broomstick();
        broomstick.setSerialNumber("SN-123");
        broomstick.setPurchasePrice(-10.0);
        broomstick.setCurrentCondition(50);

        Set<ConstraintViolation<Broomstick>> violations = validator.validate(broomstick);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("purchasePrice")));
    }

    @Test
    @DisplayName("Should fail validation when condition is outside the 0-100 range")
    void validate_ConditionOutOfRange_HasViolations() {
        Broomstick broomstick = new Broomstick();
        broomstick.setSerialNumber("SN-123");
        broomstick.setPurchasePrice(100.0);
        broomstick.setCurrentCondition(101);

        Set<ConstraintViolation<Broomstick>> violations = validator.validate(broomstick);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currentCondition")));
    }

    @Test
    @DisplayName("Should verify mapping of complex relationships through Lombok and Manual Assignment")
    void testRelationshipsAndLombok() {
        UUID id = UUID.randomUUID();
        BroomModel model = new BroomModel();
        model.setModelName("Nimbus 2001");

        Broomstick broomstick = new Broomstick();
        broomstick.setId(id);
        broomstick.setModel(model);
        broomstick.setActivities(new ArrayList<>());

        assertEquals(id, broomstick.getId());
        assertEquals("Nimbus 2001", broomstick.getModel().getModelName());
        assertNotNull(broomstick.getActivities());
    }

    @Test
    @DisplayName("Should verify AllArgsConstructor initializes the full entity state")
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        BroomModel model = new BroomModel();
        Broomstick broomstick = new Broomstick(id, "SN-TEST", 1200.0, 95, model, new ArrayList<>(), null);

        assertAll(
            () -> assertEquals(id, broomstick.getId()),
            () -> assertEquals("SN-TEST", broomstick.getSerialNumber()),
            () -> assertEquals(95, broomstick.getCurrentCondition()),
            () -> assertEquals(model, broomstick.getModel())
        );
    }
}
package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


class FlightViolationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation when issue date is in the past or present")
    void validate_ValidIssueDate_NoViolations() {
        FlightViolation violation = new FlightViolation();
        violation.setDescription("Excessive speed in a no-fly zone.");
        violation.setSeverityLevel(5);
        violation.setIsResolved(false);
        violation.setIssueDate(LocalDateTime.now().minusDays(1));
        violation.setOwner(new BroomstickOwner());

        Set<ConstraintViolation<FlightViolation>> violations = validator.validate(violation);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when issue date is in the future")
    void validate_FutureIssueDate_HasViolations() {
        FlightViolation violation = new FlightViolation();
        violation.setIssueDate(LocalDateTime.now().plusDays(10));

        Set<ConstraintViolation<FlightViolation>> violations = validator.validate(violation);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("issueDate")));
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters for core fields")
    void testLombokAccessors() {
        UUID id = UUID.randomUUID();
        BroomstickOwner owner = new BroomstickOwner();
        FlightViolation violation = new FlightViolation();

        violation.setId(id);
        violation.setDescription("Flying under the influence.");
        violation.setSeverityLevel(9);
        violation.setIsResolved(true);
        violation.setOwner(owner);

        assertEquals(id, violation.getId());
        assertEquals("Flying under the influence.", violation.getDescription());
        assertEquals(9, violation.getSeverityLevel());
        assertTrue(violation.getIsResolved());
        assertEquals(owner, violation.getOwner());
    }

    @Test
    @DisplayName("Should correctly handle the many-to-one relationship with owner")
    void testOwnerRelationship() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setFirstName("Luka");

        FlightViolation violation = new FlightViolation();
        violation.setOwner(owner);

        assertNotNull(violation.getOwner());
        assertEquals("Luka", violation.getOwner().getFirstName());
    }

    @Test
    @DisplayName("Should verify equals and hashCode based on all fields via @Data")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        FlightViolation v1 = new FlightViolation();
        v1.setId(id);
        v1.setSeverityLevel(1);

        FlightViolation v2 = new FlightViolation();
        v2.setId(id);
        v2.setSeverityLevel(1);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}
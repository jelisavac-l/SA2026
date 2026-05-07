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
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

class BroomstickOwnerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation when all owner fields and inherited wizard fields are valid")
    void validate_ValidOwner_NoViolations() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setFirstName("Luka");
        owner.setLastName("Jelisavac");
        owner.setHomeAddress("Semira Cerica Koketa 57");
        owner.setTotalFlightHours(150);
        owner.setBirthDate(LocalDateTime.now().minusYears(25));
        owner.setMagicalLevel(85);

        Set<ConstraintViolation<BroomstickOwner>> violations = validator.validate(owner);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when home address is blank")
    void validate_BlankHomeAddress_HasViolations() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setHomeAddress(" ");
        owner.setTotalFlightHours(10);
        owner.setBirthDate(LocalDateTime.now().minusYears(20));
        owner.setMagicalLevel(50);

        Set<ConstraintViolation<BroomstickOwner>> violations = validator.validate(owner);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("homeAddress")));
    }

    @Test
    @DisplayName("Should fail validation when birth date is in the future")
    void validate_FutureBirthDate_HasViolations() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setHomeAddress("Belgrade");
        owner.setBirthDate(LocalDateTime.now().plusDays(1));
        owner.setMagicalLevel(50);

        Set<ConstraintViolation<BroomstickOwner>> violations = validator.validate(owner);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthDate")));
    }

    @Test
    @DisplayName("Should fail validation when magical level is outside the 1-100 range")
    void validate_MagicalLevelOutOfRange_HasViolations() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setHomeAddress("Belgrade");
        owner.setBirthDate(LocalDateTime.now().minusYears(30));
        owner.setMagicalLevel(101);

        Set<ConstraintViolation<BroomstickOwner>> violations = validator.validate(owner);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("magicalLevel")));
    }

    @Test
    @DisplayName("Should fail validation when flight hours are negative")
    void validate_NegativeFlightHours_HasViolations() {
        BroomstickOwner owner = new BroomstickOwner();
        owner.setHomeAddress("Belgrade");
        owner.setTotalFlightHours(-1);
        owner.setBirthDate(LocalDateTime.now().minusYears(20));
        owner.setMagicalLevel(50);

        Set<ConstraintViolation<BroomstickOwner>> violations = validator.validate(owner);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("totalFlightHours")));
    }

    @Test
    @DisplayName("Should verify inherited fields and association list initialization")
    void testInheritanceAndAssociations() {
        UUID id = UUID.randomUUID();
        BroomstickOwner owner = new BroomstickOwner();
        owner.setId(id);
        owner.setFirstName("Luka");
        owner.setViolations(new ArrayList<>());

        assertEquals(id, owner.getId());
        assertEquals("Luka", owner.getFirstName());
        assertNotNull(owner.getViolations());
    }

    @Test
    @DisplayName("Should verify AllArgsConstructor including inherited and specific fields")
    void testAllArgsConstructor() {
        LocalDateTime dob = LocalDateTime.now().minusYears(22);
        BroomstickOwner owner = new BroomstickOwner("Belgrade", 100, dob, 90, new ArrayList<>());

        assertAll(
            () -> assertEquals("Belgrade", owner.getHomeAddress()),
            () -> assertEquals(100, owner.getTotalFlightHours()),
            () -> assertEquals(dob, owner.getBirthDate()),
            () -> assertEquals(90, owner.getMagicalLevel()),
            () -> assertNotNull(owner.getViolations())
        );
    }
}
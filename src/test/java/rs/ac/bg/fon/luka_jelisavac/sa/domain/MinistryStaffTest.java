package rs.ac.bg.fon.luka_jelisavac.sa.domain;

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

import static org.junit.jupiter.api.Assertions.*;

class MinistryStaffTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation when all staff fields and inherited wizard fields are valid")
    void validate_ValidStaff_NoViolations() {
        MinistryStaff staff = new MinistryStaff();
        staff.setFirstName("Luka");
        staff.setLastName("Jelisavac");
        staff.setDepartment("Department of Magical Transportation");
        staff.setRank("Senior Inspector");
        staff.setClearanceLevel(7);
        staff.setHireDate(LocalDateTime.now().minusYears(2));
        staff.setSpecialization("Broomstick Aerodynamics");

        Set<ConstraintViolation<MinistryStaff>> violations = validator.validate(staff);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when clearance level is above 10")
    void validate_ClearanceLevelTooHigh_HasViolations() {
        MinistryStaff staff = new MinistryStaff();
        staff.setClearanceLevel(11);
        staff.setHireDate(LocalDateTime.now());

        Set<ConstraintViolation<MinistryStaff>> violations = validator.validate(staff);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clearanceLevel")));
    }

    @Test
    @DisplayName("Should fail validation when clearance level is below 1")
    void validate_ClearanceLevelTooLow_HasViolations() {
        MinistryStaff staff = new MinistryStaff();
        staff.setClearanceLevel(0);
        staff.setHireDate(LocalDateTime.now());

        Set<ConstraintViolation<MinistryStaff>> violations = validator.validate(staff);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clearanceLevel")));
    }

    @Test
    @DisplayName("Should fail validation when hire date is in the future")
    void validate_FutureHireDate_HasViolations() {
        MinistryStaff staff = new MinistryStaff();
        staff.setClearanceLevel(5);
        staff.setHireDate(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<MinistryStaff>> violations = validator.validate(staff);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("hireDate")));
    }

    @Test
    @DisplayName("Should verify inheritance from Wizard and access to specific fields")
    void testInheritanceAndSpecificFields() {
        UUID id = UUID.randomUUID();
        MinistryStaff staff = new MinistryStaff();
        staff.setId(id);
        staff.setDepartment("Regulatory Body");
        staff.setSpecialization("Enforcement");

        assertEquals(id, staff.getId());
        assertEquals("Regulatory Body", staff.getDepartment());
        assertEquals("Enforcement", staff.getSpecialization());
    }

    @Test
    @DisplayName("Should verify AllArgsConstructor initializes the full state")
    void testAllArgsConstructor() {
        LocalDateTime hireDate = LocalDateTime.now().minusMonths(6);
        MinistryStaff staff = new MinistryStaff("Logistics", "Junior Officer", 3, hireDate, "Inventory Management");

        assertAll(
            () -> assertEquals("Logistics", staff.getDepartment()),
            () -> assertEquals("Junior Officer", staff.getRank()),
            () -> assertEquals(3, staff.getClearanceLevel()),
            () -> assertEquals(hireDate, staff.getHireDate()),
            () -> assertEquals("Inventory Management", staff.getSpecialization())
        );
    }
}
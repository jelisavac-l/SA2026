package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OwnershipTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters for all ownership fields")
    void testLombokAccessors() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BroomstickOwner owner = new BroomstickOwner();
        Broomstick broomstick = new Broomstick();

        Ownership ownership = new Ownership();
        ownership.setId(id);
        ownership.setStartDate(now);
        ownership.setStatus("ACTIVE");
        ownership.setOwner(owner);
        ownership.setBroomstick(broomstick);

        assertAll(
            () -> assertEquals(id, ownership.getId()),
            () -> assertEquals(now, ownership.getStartDate()),
            () -> assertEquals("ACTIVE", ownership.getStatus()),
            () -> assertEquals(owner, ownership.getOwner()),
            () -> assertEquals(broomstick, ownership.getBroomstick())
        );
    }

    @Test
    @DisplayName("Should pass validation when fields are populated (no strict constraints found)")
    void validate_ValidOwnership_NoViolations() {
        Ownership ownership = new Ownership();
        ownership.setStatus("TRANSFERRED");
        ownership.setStartDate(LocalDateTime.now());

        var violations = validator.validate(ownership);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify relationship integrity when objects are assigned")
    void testRelationshipAssignment() {
        Broomstick broom = new Broomstick();
        broom.setSerialNumber("SN-FLY-HIGH");

        BroomstickOwner owner = new BroomstickOwner();
        owner.setFirstName("Luka");

        Ownership ownership = new Ownership();
        ownership.setBroomstick(broom);
        ownership.setOwner(owner);

        assertNotNull(ownership.getBroomstick());
        assertEquals("SN-FLY-HIGH", ownership.getBroomstick().getSerialNumber());
        assertEquals("Luka", ownership.getOwner().getFirstName());
    }

    @Test
    @DisplayName("Should verify equals and hashCode based on all fields via @Data")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Ownership o1 = new Ownership();
        o1.setId(id);
        o1.setStatus("ACTIVE");

        Ownership o2 = new Ownership();
        o2.setId(id);
        o2.setStatus("ACTIVE");

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }
}
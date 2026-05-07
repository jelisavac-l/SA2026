package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class ConfiscationOrderTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters for all fields")
    void testLombokAccessors() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ConfiscationOrder order = new ConfiscationOrder();

        order.setId(id);
        order.setReasonCode("VIO-882");
        order.setIssueDate(now);

        assertEquals(id, order.getId());
        assertEquals("VIO-882", order.getReasonCode());
        assertEquals(now, order.getIssueDate());
    }

    @Test
    @DisplayName("Should pass validation as there are no specific constraints on fields")
    void validate_ValidOrder_NoViolations() {
        ConfiscationOrder order = new ConfiscationOrder();
        order.setReasonCode("ILLEGAL_MODIFICATION");
        order.setIssueDate(LocalDateTime.now());

        var violations = validator.validate(order);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify that two instances with same data are equal according to Lombok")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ConfiscationOrder order1 = new ConfiscationOrder();
        order1.setId(id);
        order1.setReasonCode("A1");
        order1.setIssueDate(now);

        ConfiscationOrder order2 = new ConfiscationOrder();
        order2.setId(id);
        order2.setReasonCode("A1");
        order2.setIssueDate(now);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    @DisplayName("Should correctly handle null fields as no @NotNull constraints are present")
    void validate_NullFields_NoViolations() {
        ConfiscationOrder order = new ConfiscationOrder();

        var violations = validator.validate(order);

        assertTrue(violations.isEmpty());
    }
}
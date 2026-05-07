package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SafetyInspectionTest {

    @Test
    @DisplayName("Should verify Lombok getters and setters for inspection specific fields")
    void testLombokAccessors() {
        SafetyInspection inspection = new SafetyInspection();
        LocalDateTime expiry = LocalDateTime.now().plusYears(1);

        inspection.setPassed(true);
        inspection.setStructuralIntegrityScore(95);
        inspection.setExpirationDate(expiry);

        assertAll(
            () -> assertTrue(inspection.getPassed()),
            () -> assertEquals(95, inspection.getStructuralIntegrityScore()),
            () -> assertEquals(expiry, inspection.getExpirationDate())
        );
    }

    @Test
    @DisplayName("Should verify inherited fields from BroomActivity are accessible")
    void testInheritedFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime activityDate = LocalDateTime.now();

        SafetyInspection inspection = new SafetyInspection();
        inspection.setId(id);
        inspection.setDate(activityDate);

        assertEquals(id, inspection.getId());
        assertEquals(activityDate, inspection.getDate());
    }

    @Test
    @DisplayName("Should verify equals and hashCode include both parent and child fields via callSuper")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime expiry = LocalDateTime.now().plusMonths(6);

        SafetyInspection inspection1 = new SafetyInspection();
        inspection1.setId(id);
        inspection1.setPassed(true);
        inspection1.setExpirationDate(expiry);

        SafetyInspection inspection2 = new SafetyInspection();
        inspection2.setId(id);
        inspection2.setPassed(true);
        inspection2.setExpirationDate(expiry);

        assertEquals(inspection1, inspection2);
        assertEquals(inspection1.hashCode(), inspection2.hashCode());
    }

    @Test
    @DisplayName("Should handle negative or edge case scores correctly as data holders")
    void testScoreEdgeCases() {
        SafetyInspection inspection = new SafetyInspection();
        inspection.setStructuralIntegrityScore(-1); // Valid for a POJO unless @Min is added

        assertEquals(-1, inspection.getStructuralIntegrityScore());
    }

    @Test
    @DisplayName("Should verify toString includes SafetyInspection specific fields")
    void testToString() {
        SafetyInspection inspection = new SafetyInspection();
        inspection.setPassed(false);
        inspection.setStructuralIntegrityScore(30);

        String toString = inspection.toString();

        assertTrue(toString.contains("passed=false"));
        assertTrue(toString.contains("structuralIntegrityScore=30"));
    }
}
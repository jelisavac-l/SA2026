package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceRecordTest {

    @Test
    @DisplayName("Should verify Lombok getters and setters for maintenance specific fields")
    void testLombokAccessors() {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setWorkDone("Replaced tail twigs and polished handle.");
        record.setCost(45.50);

        assertEquals("Replaced tail twigs and polished handle.", record.getWorkDone());
        assertEquals(45.50, record.getCost());
    }

    @Test
    @DisplayName("Should verify inherited fields from BroomActivity")
    void testInheritedFields() {
        UUID id = UUID.randomUUID();
        MaintenanceRecord record = new MaintenanceRecord();
        record.setId(id);

        assertEquals(id, record.getId());
    }

    @Test
    @DisplayName("Should verify equals and hashCode include parent and child fields")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        MaintenanceRecord record1 = new MaintenanceRecord();
        record1.setId(id);
        record1.setWorkDone("Checkup");
        record1.setCost(10.0);

        MaintenanceRecord record2 = new MaintenanceRecord();
        record2.setId(id);
        record2.setWorkDone("Checkup");
        record2.setCost(10.0);

        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());
    }

    @Test
    @DisplayName("Should handle null values for optional maintenance fields")
    void testNullFields() {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setWorkDone(null);
        record.setCost(null);

        assertNull(record.getWorkDone());
        assertNull(record.getCost());
    }
}
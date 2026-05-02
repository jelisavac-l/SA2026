package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Record for maintenance type of broom activity.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceRecord extends BroomActivity {

    /**
     * Detailed description of the technical or magical interventions performed.
     */
    private String workDone;

    /**
     * Total financial expenditure incurred for the maintenance service.
     */
    private Double cost;
}

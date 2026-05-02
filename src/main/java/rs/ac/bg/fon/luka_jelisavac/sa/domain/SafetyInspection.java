package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * Record for safety inspection type of broom activity.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SafetyInspection extends BroomActivity {

    /**
     * Final determination of the regulatory assessment.
     */
    private Boolean passed;

    /**
     * Numerical evaluation of the physical and magical stability of the broom.
     */
    private Integer structuralIntegrityScore;

    /**
     * The timestamp when the current certification loses legal validity.
     */
    private LocalDateTime expirationDate;
}

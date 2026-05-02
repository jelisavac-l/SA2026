package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Part of BroomstickOwner composition.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
public class FlightViolation {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Detailed factual account of the flying infraction.
     */
    private String description;

    /**
     * Numerical ranking of the offense's impact on public safety.
     */
    private Integer severityLevel;

    /**
     * Status flag indicating if legal obligations or fines have been met.
     */
    private Boolean isResolved;

    /**
     * Date when violation was registered.
     * Must be in the past or at the moment of issuing.
     */
    @PastOrPresent
    private LocalDateTime issueDate;
}

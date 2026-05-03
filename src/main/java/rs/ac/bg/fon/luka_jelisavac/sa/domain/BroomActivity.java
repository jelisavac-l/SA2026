package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base for all broom-related events.
 * Linked to MinistryStaff (the performer) and the Broomstick (the subject).
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BroomActivity {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Date when activity was executed. Must not be empty.
     */
    @NotNull
    private LocalDateTime date;

    /**
     * The measured intensity of the magical resonance emitted by the broomstick.
     * This value is captured via specialized Ministry sensors to detect fluctuations
     * in the underlying propulsion and stability charms.
     */
    private Double magicalAuraReading;

    /**
     * Ministry official who preformed the action.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private MinistryStaff performedBy;

    /**
     * Relationship defined in base class so subclasses can access it
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broomstick_id")
    private Broomstick broomstick;
}

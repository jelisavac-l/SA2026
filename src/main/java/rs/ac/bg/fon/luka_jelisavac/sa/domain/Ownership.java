package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Join entity representing the relationship between a Wizard and a Broomstick.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
public class Ownership {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The timestamp marking the commencement of the legal possession.
     */
    private LocalDateTime startDate;

    /**
     * Current administrative state of the ownership (e.g., ACTIVE, TRANSFERRED, REVOKED).
     */
    private String status;

    /**
     * Reference to the wizard holding legal rights to the broom.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private BroomstickOwner owner;

    /**
     * Reference to the physical unit being owned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Broomstick broomstick;
}

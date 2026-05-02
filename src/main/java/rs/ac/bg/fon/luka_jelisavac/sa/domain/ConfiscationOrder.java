package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Legal document for broom seizure.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
public class ConfiscationOrder {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The specific regulatory statute or violation identifier.
     */
    private String reasonCode;

    /**
     * Timestamp indicating when the seizure was legally authorized.
     */
    private LocalDateTime issueDate;
}

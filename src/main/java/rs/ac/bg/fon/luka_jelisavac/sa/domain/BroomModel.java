package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents the blueprint/technical specification for a broom line.
 * Shared across multiple physical broomsticks (Aggregation).
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroomModel {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The unique commercial or technical identifier assigned to a specific broomstick design.
     * This name distinguishes between different product lines (e.g., "Nimbus 2000" vs. "Cleansweep Eleven").
     * Must be unique and non-empty.
     */
    @NotBlank
    @Column(unique = true)
    private String modelName;

    /**
     * Top speed of a given model. Must be a positive real number.
     */
    @Positive
    private Double topSpeed;

    /**
     * Year of the first release of a given model.
     */
    private Integer releaseYear;
}

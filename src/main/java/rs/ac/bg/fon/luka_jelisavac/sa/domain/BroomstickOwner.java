package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a civilian broom owner.
 * Contains demographic and regulatory compliance data.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BroomstickOwner extends Wizard {
    /**
     * Official residence for Ministry legal correspondence.
     * Mandatory; cannot be blank or empty.
     */
    @NotBlank
    private String homeAddress;

    /**
     * Cumulative log of airborne duration.
     * Must be a positive integer; used for seniority and licensing tiers.
     */
    @Min(0)
    private Integer totalFlightHours;

    /**
     * The owner's legal date of birth.
     * Mandatory; must be a past date to calculate legal flying age.
     */
    @NotNull
    @Past
    private LocalDateTime birthDate;

    /**
     * Quantified magical aptitude score.
     * Must be within the standard [1, 100] Ministry scale.
     */
    @Min(1) @Max(100)
    private Integer magicalLevel;

    /**
     * Lifecycle-dependent list of legal infractions.
     * Composition: Violations are tied to the owner's lifecycle.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    private List<FlightViolation> violations;
}

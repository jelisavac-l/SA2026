package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * The physical broomstick instance.
 * Central entity for activities, ownership, and confiscations.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Broomstick {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The unique alphanumeric identifier assigned to a specific physical broomstick unit.
     * This serial number is typically engraved on the handle and serves as the primary
     * key for tracking a broom's entire lifecycle. Must be unique and non-empty.
     */
    @NotBlank
    @Column(unique = true)
    private String serialNumber;

    /**
     * Price at which the broom was purchased. Must be a positive real number.
     */
    @Positive
    private Double purchasePrice;

    /**
     * Last determined condition index. Index of 100 indicates flawless state, index of
     * 0 indicates unusable/destroyed unit.
     * Must be a positive integer in range [0, 100].
     */
    @Min(0) @Max(100)
    private Integer currentCondition;

    /**
     * Reference to the technical blueprint; must exist prior to broom registration.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private BroomModel model;

    /**
     * Historical record of events; tied to the broom's lifecycle.
     * Composition: Activities cannot exist without the broomstick.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "broomstick_id")
    private List<BroomActivity> activities;

    /**
     * Current legal seizure status, if applicable.
     * Direct association for legal status.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "confiscation_order_id")
    private ConfiscationOrder confiscationOrder;
}
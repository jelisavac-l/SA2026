package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Abstract base entity representing a magical individual.
 * Uses JOINED inheritance to separate common identity from specific roles.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Wizard {

    /**
     * Unique ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The legal first name of the individual.
     * Mandatory; must contain at least one non-whitespace character.
     */
    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false)
    private String firstName;

    /**
     * The legal last name or family name of the individual.
     * Mandatory; ensures accurate identification within Ministry records.
     */
    @NotBlank(message = "Last name is mandatory")
    @Column(nullable = false)
    private String lastName;
}

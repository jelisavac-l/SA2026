package rs.ac.bg.fon.luka_jelisavac.sa.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing Ministry personnel authorized to perform regulatory tasks.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MinistryStaff extends Wizard {

    /**
     * The specific Ministry branch or administrative unit of employment.
     */
    private String department;


    /**
     * Official professional title or seniority designation.
     */
    private String rank;

    /**
     * Security access tier for sensitive regulatory data.
     * Integer in [1, 10]; regulates access to restricted files.
     */
    @Min(1) @Max(10)
    private Integer clearanceLevel;

    /**
     * The formal commencement date of Ministry service.
     * Constraint: Must be a past or present timestamp.
     */
    @PastOrPresent
    private LocalDateTime hireDate;

    /**
     * Technical area of expertise or magic-technical focus.
     */
    private String specialization;
}

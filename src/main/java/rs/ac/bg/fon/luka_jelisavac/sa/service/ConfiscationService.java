package rs.ac.bg.fon.luka_jelisavac.sa.service;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.FlightViolationRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service managing the legal seizure and release of broomsticks.
 * Coordinates between flight violations and the physical state of the asset.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ConfiscationService {

    private final FlightViolationRepository violationRepository;
    private final BroomstickRepository broomstickRepository;

    /**
     * Records a new confiscation event triggered by a violation.
     * Automatically sets the issue date to the current system time.
     * @param violation The violation entity containing the reason and severity.
     * @return The persisted violation record.
     */
    public FlightViolation issueConfiscation(FlightViolation violation) {
        violation.setIssueDate(LocalDateTime.now());
        violation.setIsResolved(false);
        return violationRepository.save(violation);
    }

    /**
     * Evaluates if a broomstick must be grounded based on its latest violation.
     * If severity level is 8 or higher, the broom is flagged for immediate seizure.
     * @param violationId The UUID of the violation to check.
     * @return Boolean indicating if the broomstick is legally grounded.
     */
    @Transactional(readOnly = true)
    public boolean requiresImmediateSeizure(UUID violationId) {
        return violationRepository.findById(violationId)
            .map(v -> v.getSeverityLevel() != null && v.getSeverityLevel() >= 8)
            .orElse(false);
    }

    /**
     * Marks a violation as resolved and updates the legal standing.
     * @param violationId The UUID of the violation record.
     * @throws EntityNotFoundException If violation record is not found.
     */
    public void resolveViolation(UUID violationId) {
        FlightViolation violation = violationRepository.findById(violationId)
            .orElseThrow(() -> new EntityNotFoundException("Violation record not found."));

        violation.setIsResolved(true);
        violationRepository.save(violation);
    }

    /**
     * Logic to ground a broomstick physically by dropping its condition score.
     * Constraint: A confiscated broom is assigned a condition of 0 until inspected.
     * @param broomstickId The UUID of the broom being seized.
     * @throws EntityNotFoundException If broomstick is not found.
     */
    public void groundBroomstick(UUID broomstickId) {
        Broomstick broom = broomstickRepository.findById(broomstickId)
            .orElseThrow(() -> new EntityNotFoundException("Broomstick not found."));

        broom.setCurrentCondition(0);
        broomstickRepository.save(broom);
    }
}
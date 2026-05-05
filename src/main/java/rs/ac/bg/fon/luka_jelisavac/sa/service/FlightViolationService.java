package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.FlightViolationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing flight infractions and legal compliance.
 * Tracks violations committed by broomstick owners.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FlightViolationService {

    private final FlightViolationRepository violationRepository;
    private final BroomstickOwnerRepository ownerRepository;

    /**
     * Records a new violation against a specific owner.
     * Sets the issue date and default resolution status.
     * @param ownerId   UUID of the wizard who committed the infraction.
     * @param violation The violation details from the request.
     * @return The saved FlightViolation record.
     */
    public FlightViolation recordViolation(UUID ownerId, FlightViolation violation) {
        BroomstickOwner owner = ownerRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("Owner not found."));

        // Ensure the relationship is established
        violation.setOwner(owner);

        // Manual override of metadata if not provided by the frontend
        if (violation.getIssueDate() == null) {
            violation.setIssueDate(LocalDateTime.now());
        }
        violation.setIsResolved(false);

        return violationRepository.save(violation);
    }

    /**
     * Updates the status to resolved.
     * @param violationId UUID of the violation.
     */
    public void resolveViolation(UUID violationId) {
        FlightViolation violation = violationRepository.findById(violationId)
            .orElseThrow(() -> new EntityNotFoundException("Violation not found."));

        violation.setIsResolved(true);
        violationRepository.save(violation);
    }

    /**
     * Retrieves all unresolved violations for a specific owner.
     * @param ownerId UUID of the owner.
     * @return List of owners violations.
     */
    @Transactional(readOnly = true)
    public List<FlightViolation> getUnresolvedByOwner(UUID ownerId) {
        // Assuming your repository has a custom query or derived method
        return violationRepository.findByOwnerId(ownerId)
            .stream()
            .filter(v -> Boolean.FALSE.equals(v.getIsResolved()))
            .toList();
    }

    /**
     * General lookup for a violation record.
     * @param id UUID of the violation.
     * @return Violation with given UUID.
     * @throws EntityNotFoundException If violation is not found.
     */
    @Transactional(readOnly = true)
    public FlightViolation getViolationById(UUID id) {
        return violationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Violation record with ID " + id + " does not exist."));
    }
}

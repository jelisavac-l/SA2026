package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Ownership;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.OwnershipRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing legal ownership records.
 * Handles the assignment, status transitions, and history of broomstick possession.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OwnershipService {

    private final OwnershipRepository ownershipRepository;
    private final BroomstickRepository broomstickRepository;
    private final BroomstickOwnerRepository ownerRepository;

    /**
     * Establishes a new ownership link.
     * Sets the status to 'ACTIVE' and records the current timestamp as the start date.
     * @param broomstickId UUID of the physical broom.
     * @param ownerId      UUID of the wizard.
     * @return The created Ownership record.
     * @throws EntityNotFoundException If broomstick was not found.
     * @throws EntityNotFoundException If owner was not found.
     */
    public Ownership assignBroomstick(UUID broomstickId, UUID ownerId) {
        Broomstick broomstick = broomstickRepository.findById(broomstickId)
            .orElseThrow(() -> new EntityNotFoundException("Broomstick not found."));
        BroomstickOwner owner = ownerRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("Owner not found."));

        Ownership ownership = new Ownership();
        ownership.setBroomstick(broomstick);
        ownership.setOwner(owner);
        ownership.setStartDate(LocalDateTime.now());
        ownership.setStatus("ACTIVE");

        return ownershipRepository.save(ownership);
    }

    /**
     * Updates the administrative state of a specific ownership record.
     * @param id        The UUID of the ownership entry.
     * @param newStatus The target status (e.g., TRANSFERRED, REVOKED).
     * @throws EntityNotFoundException If ownership record was not found.
     */
    public void updateOwnershipStatus(UUID id, String newStatus) {
        Ownership ownership = ownershipRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ownership record not found."));

        ownership.setStatus(newStatus.toUpperCase());
        ownershipRepository.save(ownership);
    }

    /**
     * Retrieves the current active ownership for a specific broomstick.
     * Filters by status to ensure only the legal possessor is returned.
     * @param broomstickId UUID of the physical broom.
     * @return Ownership record.
     * @throws EntityNotFoundException If no active owner was found.
     */
    @Transactional(readOnly = true)
    public Ownership findActiveOwnership(UUID broomstickId) {
        return ownershipRepository.findByBroomstickId(broomstickId)
            .stream()
            .filter(o -> "ACTIVE".equalsIgnoreCase(o.getStatus()))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("No active owner found for this unit."));
    }

    /**
     * Returns all ownership records associated with a specific wizard.
     * Useful for auditing a wizard's history of assets.
     * @param ownerId UUID of the wizard.
     * @return List of wizards broom ownership records.
     */
    @Transactional(readOnly = true)
    public List<Ownership> getOwnerHistory(UUID ownerId) {
        return ownershipRepository.findByOwnerIdOrderByStartDateDesc(ownerId);
    }
}
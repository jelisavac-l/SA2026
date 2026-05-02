package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;

import java.util.UUID;

/**
 * Primary service for managing physical broomstick assets and their lifecycle.
 * Handles registration, inventory tracking, and initial flight readiness assessments.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BroomstickService {
    private final BroomstickRepository broomstickRepository;

    /**
     * Records a new broomstick in the Ministry database.
     * Constraint: Serial number must be unique and not blank.
     * @param broomstick The entity to persist.
     * @return The saved broomstick with an assigned UUID.
     */
    public Broomstick registerBroomstick(Broomstick broomstick) {
        return broomstickRepository.save(broomstick);
    }

    /**
     * Locates a specific unit using its engraved serial number.
     * @param serialNumber The unique identifier.
     * @return The found Broomstick entity.
     * @throws EntityNotFoundException If no unit matches the provided serial.
     */
    @Transactional(readOnly = true)
    public Broomstick findBySerial(String serialNumber) {
        return broomstickRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new EntityNotFoundException("Broomstick not found: " + serialNumber));
    }

    /**
     * Evaluates flight safety based on the condition score.
     * Constraint: A score below 40 is considered a safety hazard by the Ministry.
     * @param id The UUID of the broom.
     * @return Boolean value indicating whether it's safe to fly.
     * @throws EntityNotFoundException If no unit matches the provided id.
     */
    @Transactional(readOnly = true)
    public boolean isSafeForFlight(UUID id) {
        return broomstickRepository.findById(id)
            .map(broom -> {
                Integer condition = broom.getCurrentCondition();
                return condition != null && condition >= 40;
            })
            .orElseThrow(() -> new EntityNotFoundException("Unit not found."));
    }

    /**
     * Updates the physical status score of the unit.
     * @param id The UUID of the broom.
     * @param newConditionScore Integer value representing the new state.
     * @throws EntityNotFoundException If no unit matches the provided id.
     */
    public Broomstick updateCondition(UUID id, Integer newConditionScore) {
        Broomstick existing = broomstickRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Unit not found."));

        existing.setCurrentCondition(newConditionScore);
        return broomstickRepository.save(existing);
    }
}

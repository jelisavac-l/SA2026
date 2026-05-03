package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;

import java.util.UUID;

/**
 * Service for handling logic for registered broomstick owners.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BroomstickOwnerService {

    private final BroomstickOwnerRepository ownerRepository;

    /**
     * Registers a new owner in the Ministry database. Initializes flight hours to 0 by default.
     * @param owner Broomstick owner object to be persisted to the database.
     * @return Saved broomstick owner object.
     */
    public BroomstickOwner registerOwner(BroomstickOwner owner) {
        if (owner.getTotalFlightHours() == null) {
            owner.setTotalFlightHours(0);
        }
        return ownerRepository.save(owner);
    }

    /**
     * Logs flight hours to the owner's permanent record.
     * Constraint: Validates that flight hours are positive.
     * @param ownerId UUID of the broomstick owner.
     * @param hours Amount of flight hours to be appended. Must be a non-negative integer.
     * @throws IllegalArgumentException If provided hours are a negative number.
     * @throws EntityNotFoundException If owner is not found.
     */
    public void addFlightHours(UUID ownerId, int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative.");

        BroomstickOwner owner = ownerRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("Owner not found."));

        owner.setTotalFlightHours(owner.getTotalFlightHours() + hours);
        ownerRepository.save(owner);
    }
}

package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Wizard;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.WizardRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service for handling shared logic of both broomstick owners and
 * ministry personnel.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    /**
     * Searches for a wizard based on provided last name, case-insensitive.
     * @param lastName Last name of the wizard.
     * @return Wizard(s) with given last name. Empty list if there are none.
     */
    @Transactional(readOnly = true)
    public List<Wizard> searchByName(String lastName) {
        return wizardRepository.findByLastNameIgnoreCase(lastName);
    }

    /**
     * Searches for a wizard based on provided UUID.
     * @param id UUID of the wizard.
     * @return Wizard with given UUID.
     * @throws EntityNotFoundException If wizard with given UUID is not found.
     */
    @Transactional(readOnly = true)
    public Wizard getWizard(UUID id) {
        return wizardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Wizard not found."));
    }
}
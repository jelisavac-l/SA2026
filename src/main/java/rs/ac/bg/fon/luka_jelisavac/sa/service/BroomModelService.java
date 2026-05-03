package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomModel;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomModelRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing the technical blueprints and specifications of broom lines.
 * Acts as the definitive registry for commercial models allowed in the market.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BroomModelService {

    private final BroomModelRepository broomModelRepository;

    /**
     * Registers a new broom line specification into the Ministry registry.
     * The modelName must be unique to prevent commercial duplication.
     * @param model The blueprint data to be saved.
     * @return The persisted BroomModel.
     * @throws IllegalStateException If model with the passed name already exists.
     */
    public BroomModel registerModel(BroomModel model) {
        if (broomModelRepository.existsByModelName(model.getModelName())) {
            throw new IllegalStateException("A model with the name " + model.getModelName() + " already exists.");
        }
        return broomModelRepository.save(model);
    }

    /**
     * Retrieves all registered broom specifications.
     * Used for populating catalogs or administrative dropdowns.
     * @return All models.
     */
    @Transactional(readOnly = true)
    public List<BroomModel> getAllModels() {
        return broomModelRepository.findAll();
    }

    /**
     * Finds a specific technical blueprint by its UUID.
     * @param id The unique identifier of the model.
     * @return Model with the passed ID.
     * @throws EntityNotFoundException If model with passed ID is not found.
     */
    @Transactional(readOnly = true)
    public BroomModel getModelById(UUID id) {
        return broomModelRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Broom model specification not found."));
    }

    /**
     * Updates the technical performance specs (like top speed) for an existing line.
     * Model name will not be updated to maintain referential integrity.
     * @param id The unique identifier of the model.
     * @param updatedSpecs Model object with new values.
     * @return Updated model.
     */
    public BroomModel updateModelSpecs(UUID id, BroomModel updatedSpecs) {
        BroomModel existing = getModelById(id);

        existing.setTopSpeed(updatedSpecs.getTopSpeed());
        existing.setReleaseYear(updatedSpecs.getReleaseYear());

        return broomModelRepository.save(existing);
    }

    /**
     * Deletes a model from the registry.
     * Note: In a production environment, you'd check for existing physical
     * broomsticks linked to this model before allowing deletion.
     * @param id The unique identifier of the model.
     * @throws EntityNotFoundException If model with passed ID is not found.
     */
    public void retireModel(UUID id) {
        if (!broomModelRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot retire non-existent model.");
        }
        broomModelRepository.deleteById(id);
    }
}

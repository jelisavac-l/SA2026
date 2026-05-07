package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomModel;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomModelRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroomModelServiceTest {

    @Mock
    private BroomModelRepository broomModelRepository;

    @InjectMocks
    private BroomModelService broomModelService;

    private UUID modelId;
    private BroomModel broomModel;

    @BeforeEach
    void setUp() {
        modelId = UUID.randomUUID();
        broomModel = new BroomModel();
        broomModel.setId(modelId);
        broomModel.setModelName("Nimbus 2000");
        broomModel.setTopSpeed(554.22);
        broomModel.setReleaseYear(2003);
    }

    @Test
    @DisplayName("Should save broom model when name is unique")
    void registerModel_Success() {
        when(broomModelRepository.existsByModelName(broomModel.getModelName())).thenReturn(false);
        when(broomModelRepository.save(any(BroomModel.class))).thenReturn(broomModel);

        BroomModel result = broomModelService.registerModel(broomModel);

        assertNotNull(result);
        assertEquals("Nimbus 2000", result.getModelName());
        verify(broomModelRepository).save(broomModel);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when model name already exists")
    void registerModel_NameExists_ThrowsException() {
        when(broomModelRepository.existsByModelName(broomModel.getModelName())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> broomModelService.registerModel(broomModel));
        verify(broomModelRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return model when valid ID is provided")
    void getModelById_Success() {
        when(broomModelRepository.findById(modelId)).thenReturn(Optional.of(broomModel));

        BroomModel result = broomModelService.getModelById(modelId);

        assertEquals(modelId, result.getId());
        assertEquals("Nimbus 2000", result.getModelName());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when model ID does not exist")
    void getModelById_NotFound_ThrowsException() {
        when(broomModelRepository.findById(modelId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> broomModelService.getModelById(modelId));
    }

    @Test
    @DisplayName("Should update only allowed fields when updating specs")
    void updateModelSpecs_Success() {
        BroomModel updatedSpecs = new BroomModel();
        updatedSpecs.setTopSpeed(22.554);
        updatedSpecs.setReleaseYear(2026);
        updatedSpecs.setModelName("TEST TEST 1 2 3 HELLO WORLD THIS SHOULDN'T UPDATE");

        when(broomModelRepository.findById(modelId)).thenReturn(Optional.of(broomModel));
        when(broomModelRepository.save(any(BroomModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BroomModel result = broomModelService.updateModelSpecs(modelId, updatedSpecs);

        assertEquals(22.554, result.getTopSpeed());
        assertEquals(2026, result.getReleaseYear());
        assertEquals("Nimbus 2000", result.getModelName());
        verify(broomModelRepository).save(broomModel);
    }

    @Test
    @DisplayName("Should delete model when ID exists")
    void retireModel_Success() {
        when(broomModelRepository.existsById(modelId)).thenReturn(true);

        broomModelService.retireModel(modelId);

        verify(broomModelRepository).deleteById(modelId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when retiring non-existent model")
    void retireModel_NotFound_ThrowsException() {
        when(broomModelRepository.existsById(modelId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> broomModelService.retireModel(modelId));
        verify(broomModelRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return all registered models")
    void getAllModels_Success() {
        List<BroomModel> models = List.of(broomModel, new BroomModel());
        when(broomModelRepository.findAll()).thenReturn(models);

        List<BroomModel> result = broomModelService.getAllModels();

        assertEquals(2, result.size());
        verify(broomModelRepository).findAll();
    }
}
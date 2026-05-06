package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Wizard;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.WizardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    private WizardRepository wizardRepository;

    @InjectMocks
    private WizardService wizardService;

    private UUID wizardId;
    private Wizard wizard;

    @BeforeEach
    void setUp() {
        wizardId = UUID.randomUUID();
        wizard = new BroomstickOwner();
        wizard.setId(wizardId);
        wizard.setFirstName("Luka");
        wizard.setLastName("Jelisavac");
    }

    @Test
    @DisplayName("Should return list of wizards when searching by last name case-insensitively")
    void searchByName_Success() {
        String searchName = "jelisavac";
        when(wizardRepository.findByLastNameIgnoreCase(searchName)).thenReturn(List.of(wizard));

        List<Wizard> result = wizardService.searchByName(searchName);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Jelisavac", result.get(0).getLastName());
        verify(wizardRepository).findByLastNameIgnoreCase(searchName);
    }

    @Test
    @DisplayName("Should return empty list when no wizard matches the last name")
    void searchByName_NoResults_ReturnsEmptyList() {
        String searchName = "Unknown";
        when(wizardRepository.findByLastNameIgnoreCase(searchName)).thenReturn(List.of());

        List<Wizard> result = wizardService.searchByName(searchName);

        assertTrue(result.isEmpty());
        verify(wizardRepository).findByLastNameIgnoreCase(searchName);
    }

    @Test
    @DisplayName("Should return wizard when valid UUID is provided")
    void getWizard_Success() {
        when(wizardRepository.findById(wizardId)).thenReturn(Optional.of(wizard));

        Wizard result = wizardService.getWizard(wizardId);

        assertNotNull(result);
        assertEquals(wizardId, result.getId());
        verify(wizardRepository).findById(wizardId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when wizard ID does not exist")
    void getWizard_NotFound_ThrowsException() {
        when(wizardRepository.findById(wizardId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> wizardService.getWizard(wizardId));
        verify(wizardRepository).findById(wizardId);
    }
}
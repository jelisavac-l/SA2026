package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.FlightViolationRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiscationServiceTest {

    @Mock
    private FlightViolationRepository violationRepository;

    @Mock
    private BroomstickRepository broomstickRepository;

    @InjectMocks
    private ConfiscationService confiscationService;

    private UUID violationId;
    private UUID broomstickId;
    private FlightViolation violation;
    private Broomstick broomstick;

    @BeforeEach
    void setUp() {
        violationId = UUID.randomUUID();
        broomstickId = UUID.randomUUID();

        violation = new FlightViolation();
        violation.setId(violationId);
        violation.setSeverityLevel(5);

        broomstick = new Broomstick();
        broomstick.setId(broomstickId);
        broomstick.setCurrentCondition(75);
    }

    @Test
    @DisplayName("Should initialize issue date and unresolved status when issuing confiscation")
    void issueConfiscation_Success() {
        when(violationRepository.save(any(FlightViolation.class))).thenAnswer(i -> i.getArguments()[0]);

        FlightViolation result = confiscationService.issueConfiscation(violation);

        assertNotNull(result.getIssueDate());
        assertFalse(result.getIsResolved());
        verify(violationRepository).save(violation);
    }

    @Test
    @DisplayName("Should return true for seizure when severity level is 8 or higher")
    void requiresImmediateSeizure_HighSeverity_ReturnsTrue() {
        violation.setSeverityLevel(8);
        when(violationRepository.findById(violationId)).thenReturn(Optional.of(violation));

        boolean result = confiscationService.requiresImmediateSeizure(violationId);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for seizure when severity level is below 8")
    void requiresImmediateSeizure_LowSeverity_ReturnsFalse() {
        violation.setSeverityLevel(7);
        when(violationRepository.findById(violationId)).thenReturn(Optional.of(violation));

        boolean result = confiscationService.requiresImmediateSeizure(violationId);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for seizure when violation does not exist")
    void requiresImmediateSeizure_NotFound_ReturnsFalse() {
        when(violationRepository.findById(violationId)).thenReturn(Optional.empty());

        boolean result = confiscationService.requiresImmediateSeizure(violationId);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should mark violation as resolved")
    void resolveViolation_Success() {
        when(violationRepository.findById(violationId)).thenReturn(Optional.of(violation));

        confiscationService.resolveViolation(violationId);

        assertTrue(violation.getIsResolved());
        verify(violationRepository).save(violation);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when resolving non-existent violation")
    void resolveViolation_NotFound_ThrowsException() {
        when(violationRepository.findById(violationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> confiscationService.resolveViolation(violationId));
    }

    @Test
    @DisplayName("Should set broomstick condition to zero when grounded")
    void groundBroomstick_Success() {
        when(broomstickRepository.findById(broomstickId)).thenReturn(Optional.of(broomstick));

        confiscationService.groundBroomstick(broomstickId);

        assertEquals(0, broomstick.getCurrentCondition());
        verify(broomstickRepository).save(broomstick);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when grounding non-existent broomstick")
    void groundBroomstick_NotFound_ThrowsException() {
        when(broomstickRepository.findById(broomstickId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> confiscationService.groundBroomstick(broomstickId));
    }
}
package rs.ac.bg.fon.luka_jelisavac.sa.service;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.MaintenanceRecord;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.SafetyInspection;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomActivity;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomActivityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroomActivityServiceTest {

    @Mock
    private BroomActivityRepository activityRepository;

    @Mock
    private BroomstickRepository broomstickRepository;

    @InjectMocks
    private BroomActivityService broomActivityService;

    private UUID broomId;
    private Broomstick broomstick;

    @BeforeEach
    void setUp() {
        broomId = UUID.randomUUID();
        broomstick = new Broomstick();
        broomstick.setId(broomId);
        broomstick.setCurrentCondition(50);
    }

    @Test
    @DisplayName("Should update broom condition and set expiration when processing safety inspection")
    void processActivity_SafetyInspection_Success() {
        SafetyInspection inspection = new SafetyInspection();
        inspection.setStructuralIntegrityScore(80);

        when(broomstickRepository.findById(broomId)).thenReturn(Optional.of(broomstick));
        when(activityRepository.save(any(BroomActivity.class))).thenAnswer(i -> i.getArguments()[0]);

        SafetyInspection result = (SafetyInspection) broomActivityService.processActivity(broomId, inspection);

        assertEquals(80, broomstick.getCurrentCondition());
        assertNotNull(result.getExpirationDate());
        assertNotNull(result.getDate());
        assertEquals(broomstick, result.getBroomstick());
        verify(broomstickRepository).save(broomstick);
        verify(activityRepository).save(inspection);
    }

    @Test
    @DisplayName("Should maintain existing broom condition when processing maintenance record")
    void processActivity_MaintenanceRecord_Success() {
        MaintenanceRecord maintenance = new MaintenanceRecord();
        maintenance.setCost(200.0);

        when(broomstickRepository.findById(broomId)).thenReturn(Optional.of(broomstick));
        when(activityRepository.save(any(BroomActivity.class))).thenAnswer(i -> i.getArguments()[0]);

        MaintenanceRecord result = (MaintenanceRecord) broomActivityService.processActivity(broomId, maintenance);

        assertEquals(50, broomstick.getCurrentCondition());
        assertNotNull(result.getDate());
        assertEquals(broomstick, result.getBroomstick());
        verify(broomstickRepository, never()).save(any(Broomstick.class));
        verify(activityRepository).save(maintenance);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when broomstick does not exist")
    void processActivity_BroomNotFound_ThrowsException() {
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            broomActivityService.processActivity(broomId, new SafetyInspection())
        );
        verify(activityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return activity history for a specific broomstick")
    void getActivityHistory_Success() {
        List<BroomActivity> history = List.of(new SafetyInspection(), new MaintenanceRecord());
        when(activityRepository.findByBroomstickIdOrderByDateDesc(broomId)).thenReturn(history);

        List<BroomActivity> result = broomActivityService.getActivityHistory(broomId);

        assertEquals(2, result.size());
        verify(activityRepository).findByBroomstickIdOrderByDateDesc(broomId);
    }
}
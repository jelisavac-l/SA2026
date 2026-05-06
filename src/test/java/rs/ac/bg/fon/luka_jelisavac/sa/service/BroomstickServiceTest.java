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
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroomstickServiceTest {

    @Mock
    private BroomstickRepository broomstickRepository;

    @InjectMocks
    private BroomstickService broomstickService;

    private UUID broomId;
    private Broomstick broomstick;
    private final String serialNumber = "BG-554-LJ";

    @BeforeEach
    void setUp() {
        broomId = UUID.randomUUID();
        broomstick = new Broomstick();
        broomstick.setId(broomId);
        broomstick.setSerialNumber(serialNumber);
        broomstick.setCurrentCondition(100);
    }

    @Test
    @DisplayName("Should successfully persist a new broomstick record")
    void registerBroomstick_Success() {
        when(broomstickRepository.save(any(Broomstick.class))).thenReturn(broomstick);

        Broomstick result = broomstickService.registerBroomstick(broomstick);

        assertNotNull(result);
        assertEquals(serialNumber, result.getSerialNumber());
        verify(broomstickRepository).save(broomstick);
    }

    @Test
    @DisplayName("Should return broomstick when searching by valid serial number")
    void findBySerial_Success() {
        when(broomstickRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(broomstick));

        Broomstick result = broomstickService.findBySerial(serialNumber);

        assertNotNull(result);
        assertEquals(broomId, result.getId());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when serial number does not exist")
    void findBySerial_NotFound_ThrowsException() {
        when(broomstickRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> broomstickService.findBySerial(serialNumber));
    }

    @Test
    @DisplayName("Should return true for flight safety when condition score is 40 or higher")
    void isSafeForFlight_True() {
        broomstick.setCurrentCondition(40);
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.of(broomstick));

        boolean safe = broomstickService.isSafeForFlight(broomId);

        assertTrue(safe);
    }

    @Test
    @DisplayName("Should return false for flight safety when condition score is below 40")
    void isSafeForFlight_False() {
        broomstick.setCurrentCondition(39);
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.of(broomstick));

        boolean safe = broomstickService.isSafeForFlight(broomId);

        assertFalse(safe);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when checking safety for non-existent unit")
    void isSafeForFlight_NotFound_ThrowsException() {
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> broomstickService.isSafeForFlight(broomId));
    }

    @Test
    @DisplayName("Should successfully update condition score for an existing broomstick")
    void updateCondition_Success() {
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.of(broomstick));
        when(broomstickRepository.save(any(Broomstick.class))).thenAnswer(i -> i.getArguments()[0]);

        Broomstick updated = broomstickService.updateCondition(broomId, 85);

        assertEquals(85, updated.getCurrentCondition());
        verify(broomstickRepository).save(broomstick);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating condition for non-existent unit")
    void updateCondition_NotFound_ThrowsException() {
        when(broomstickRepository.findById(broomId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> broomstickService.updateCondition(broomId, 50));
        verify(broomstickRepository, never()).save(any());
    }
}
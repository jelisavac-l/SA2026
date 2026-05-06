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
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroomstickOwnerServiceTest {

    @Mock
    private BroomstickOwnerRepository ownerRepository;

    @InjectMocks
    private BroomstickOwnerService broomstickOwnerService;

    private UUID ownerId;
    private BroomstickOwner owner;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        owner = new BroomstickOwner();
        owner.setId(ownerId);
        owner.setFirstName("Luka");
        owner.setLastName("Jelisavac");
        owner.setMagicalLevel(100);
        owner.setTotalFlightHours(9000);
        owner.setHomeAddress("Nihada Kantića Šiketa 57");
    }

    @Test
    @DisplayName("Should initialize flight hours to zero when registering new owner without specified hours")
    void registerOwner_DefaultFlightHours() {
        owner.setTotalFlightHours(null);
        when(ownerRepository.save(any(BroomstickOwner.class))).thenAnswer(i -> i.getArguments()[0]);

        BroomstickOwner result = broomstickOwnerService.registerOwner(owner);

        assertEquals(0, result.getTotalFlightHours());
        verify(ownerRepository).save(owner);
    }

    @Test
    @DisplayName("Should preserve existing flight hours when registering owner with predefined hours")
    void registerOwner_PreserveExistingHours() {
        owner.setTotalFlightHours(50);
        when(ownerRepository.save(any(BroomstickOwner.class))).thenReturn(owner);

        BroomstickOwner result = broomstickOwnerService.registerOwner(owner);

        assertEquals(50, result.getTotalFlightHours());
        verify(ownerRepository).save(owner);
    }

    @Test
    @DisplayName("Should correctly increment total flight hours for existing owner")
    void addFlightHours_Success() {
        owner.setTotalFlightHours(10);
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        broomstickOwnerService.addFlightHours(ownerId, 5);

        assertEquals(15, owner.getTotalFlightHours());
        verify(ownerRepository).save(owner);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when adding negative flight hours")
    void addFlightHours_NegativeValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            broomstickOwnerService.addFlightHours(ownerId, -1)
        );
        verifyNoInteractions(ownerRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when adding hours to non-existent owner")
    void addFlightHours_OwnerNotFound_ThrowsException() {
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            broomstickOwnerService.addFlightHours(ownerId, 10)
        );
        verify(ownerRepository, never()).save(any());
    }
}
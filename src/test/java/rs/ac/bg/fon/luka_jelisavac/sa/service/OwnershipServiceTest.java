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
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Ownership;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.OwnershipRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnershipServiceTest {

    @Mock
    private OwnershipRepository ownershipRepository;

    @Mock
    private BroomstickRepository broomstickRepository;

    @Mock
    private BroomstickOwnerRepository ownerRepository;

    @InjectMocks
    private OwnershipService ownershipService;

    private UUID broomstickId;
    private UUID ownerId;
    private Broomstick broomstick;
    private BroomstickOwner owner;

    @BeforeEach
    void setUp() {
        broomstickId = UUID.randomUUID();
        ownerId = UUID.randomUUID();

        broomstick = new Broomstick();
        broomstick.setId(broomstickId);

        owner = new BroomstickOwner();
        owner.setId(ownerId);
    }

    @Test
    @DisplayName("Should successfully assign broomstick to owner with active status")
    void assignBroomstick_Success() {
        when(broomstickRepository.findById(broomstickId)).thenReturn(Optional.of(broomstick));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(ownershipRepository.save(any(Ownership.class))).thenAnswer(i -> i.getArguments()[0]);

        Ownership result = ownershipService.assignBroomstick(broomstickId, ownerId);

        assertNotNull(result.getStartDate());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(broomstick, result.getBroomstick());
        assertEquals(owner, result.getOwner());
        verify(ownershipRepository).save(any(Ownership.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when broomstick does not exist during assignment")
    void assignBroomstick_BroomNotFound_ThrowsException() {
        when(broomstickRepository.findById(broomstickId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            ownershipService.assignBroomstick(broomstickId, ownerId)
        );
        verifyNoInteractions(ownerRepository);
        verifyNoInteractions(ownershipRepository);
    }

    @Test
    @DisplayName("Should update status to uppercase when changing ownership status")
    void updateOwnershipStatus_Success() {
        UUID ownershipId = UUID.randomUUID();
        Ownership ownership = new Ownership();
        when(ownershipRepository.findById(ownershipId)).thenReturn(Optional.of(ownership));

        ownershipService.updateOwnershipStatus(ownershipId, "transferred");

        assertEquals("TRANSFERRED", ownership.getStatus());
        verify(ownershipRepository).save(ownership);
    }

    @Test
    @DisplayName("Should find active ownership record for a specific broomstick")
    void findActiveOwnership_Success() {
        Ownership activeOwnership = new Ownership();
        activeOwnership.setStatus("ACTIVE");
        Ownership inactiveOwnership = new Ownership();
        inactiveOwnership.setStatus("REVOKED");

        when(ownershipRepository.findByBroomstickId(broomstickId))
            .thenReturn(List.of(inactiveOwnership, activeOwnership));

        Ownership result = ownershipService.findActiveOwnership(broomstickId);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when no active owner is found for unit")
    void findActiveOwnership_NoActiveFound_ThrowsException() {
        Ownership revokedOwnership = new Ownership();
        revokedOwnership.setStatus("REVOKED");

        when(ownershipRepository.findByBroomstickId(broomstickId)).thenReturn(List.of(revokedOwnership));

        assertThrows(EntityNotFoundException.class, () ->
            ownershipService.findActiveOwnership(broomstickId)
        );
    }

    @Test
    @DisplayName("Should return historical ownership records for a specific wizard")
    void getOwnerHistory_Success() {
        List<Ownership> history = List.of(new Ownership(), new Ownership());
        when(ownershipRepository.findByOwnerIdOrderByStartDateDesc(ownerId)).thenReturn(history);

        List<Ownership> result = ownershipService.getOwnerHistory(ownerId);

        assertEquals(2, result.size());
        verify(ownershipRepository).findByOwnerIdOrderByStartDateDesc(ownerId);
    }
}
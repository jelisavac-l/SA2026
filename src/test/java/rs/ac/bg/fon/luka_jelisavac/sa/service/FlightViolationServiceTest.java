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
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickOwnerRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.FlightViolationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightViolationServiceTest {

    @Mock
    private FlightViolationRepository violationRepository;

    @Mock
    private BroomstickOwnerRepository ownerRepository;

    @InjectMocks
    private FlightViolationService flightViolationService;

    private UUID ownerId;
    private UUID violationId;
    private BroomstickOwner owner;
    private FlightViolation violation;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        violationId = UUID.randomUUID();

        owner = new BroomstickOwner();
        owner.setId(ownerId);

        violation = new FlightViolation();
        violation.setId(violationId);
        violation.setSeverityLevel(3);
    }

    @Test
    @DisplayName("Should successfully record violation and link it to owner")
    void recordViolation_Success() {
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(violationRepository.save(any(FlightViolation.class))).thenAnswer(i -> i.getArguments()[0]);

        FlightViolation result = flightViolationService.recordViolation(ownerId, violation);

        assertNotNull(result.getIssueDate());
        assertFalse(result.getIsResolved());
        assertEquals(owner, result.getOwner());
        verify(violationRepository).save(violation);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when recording violation for non-existent owner")
    void recordViolation_OwnerNotFound_ThrowsException() {
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            flightViolationService.recordViolation(ownerId, violation)
        );
        verify(violationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update violation status to resolved")
    void resolveViolation_Success() {
        violation.setIsResolved(false);
        when(violationRepository.findById(violationId)).thenReturn(Optional.of(violation));

        flightViolationService.resolveViolation(violationId);

        assertTrue(violation.getIsResolved());
        verify(violationRepository).save(violation);
    }

    @Test
    @DisplayName("Should filter and return only unresolved violations for owner")
    void getUnresolvedByOwner_Success() {
        FlightViolation v1 = new FlightViolation();
        v1.setIsResolved(false);
        FlightViolation v2 = new FlightViolation();
        v2.setIsResolved(true);

        when(violationRepository.findByOwnerId(ownerId)).thenReturn(List.of(v1, v2));

        List<FlightViolation> result = flightViolationService.getUnresolvedByOwner(ownerId);

        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsResolved());
    }

    @Test
    @DisplayName("Should return specific violation by ID")
    void getViolationById_Success() {
        when(violationRepository.findById(violationId)).thenReturn(Optional.of(violation));

        FlightViolation result = flightViolationService.getViolationById(violationId);

        assertEquals(violationId, result.getId());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when violation ID does not exist")
    void getViolationById_NotFound_ThrowsException() {
        when(violationRepository.findById(violationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            flightViolationService.getViolationById(violationId)
        );
    }
}
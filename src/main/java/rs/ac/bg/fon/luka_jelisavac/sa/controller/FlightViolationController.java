package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.service.FlightViolationService;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/violations")
@RequiredArgsConstructor
public class FlightViolationController {

    private final FlightViolationService violationService;

    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<FlightViolation> reportViolation(
        @PathVariable UUID ownerId,
        @RequestBody FlightViolation violation) {
        FlightViolation recorded = violationService.recordViolation(ownerId, violation);
        return new ResponseEntity<>(recorded, HttpStatus.CREATED);
    }

    @GetMapping("/owner/{ownerId}/active")
    public ResponseEntity<List<FlightViolation>> getActiveViolations(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(violationService.getUnresolvedByOwner(ownerId));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveViolation(@PathVariable UUID id) {
        violationService.resolveViolation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightViolation> getViolation(@PathVariable UUID id) {
        return ResponseEntity.ok(violationService.getViolationById(id));
    }
}

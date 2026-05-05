package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Ownership;
import rs.ac.bg.fon.luka_jelisavac.sa.service.OwnershipService;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/ownership")
@RequiredArgsConstructor
public class OwnershipController {

    private final OwnershipService ownershipService;

    @PostMapping("/assign")
    public ResponseEntity<Ownership> assignBroomstick(
        @RequestParam UUID broomstickId,
        @RequestParam UUID ownerId) {
        Ownership ownership = ownershipService.assignBroomstick(broomstickId, ownerId);
        return new ResponseEntity<>(ownership, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
        @PathVariable UUID id,
        @RequestParam String newStatus) {
        ownershipService.updateOwnershipStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active/{broomstickId}")
    public ResponseEntity<Ownership> getActive(@PathVariable UUID broomstickId) {
        return ResponseEntity.ok(ownershipService.findActiveOwnership(broomstickId));
    }

    @GetMapping("/history/owner/{ownerId}")
    public ResponseEntity<List<Ownership>> getHistory(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(ownershipService.getOwnerHistory(ownerId));
    }
}

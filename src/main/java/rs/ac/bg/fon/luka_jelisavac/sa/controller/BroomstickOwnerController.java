package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;
import rs.ac.bg.fon.luka_jelisavac.sa.service.BroomstickOwnerService;
import rs.ac.bg.fon.luka_jelisavac.sa.service.WizardService;

import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class BroomstickOwnerController {

    private final BroomstickOwnerService ownerService;
    private final WizardService wizardService;

    @PostMapping
    public ResponseEntity<BroomstickOwner> register(@RequestBody BroomstickOwner owner) {
        BroomstickOwner created = ownerService.registerOwner(owner);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BroomstickOwner> getById(@PathVariable UUID id) {
        BroomstickOwner owner = (BroomstickOwner) wizardService.getWizard(id);
        return ResponseEntity.ok(owner);
    }

    @PatchMapping("/{id}/flight-hours")
    public ResponseEntity<Void> addHours(@PathVariable UUID id, @RequestParam int hours) {
        ownerService.addFlightHours(id, hours);
        return ResponseEntity.noContent().build();
    }
}
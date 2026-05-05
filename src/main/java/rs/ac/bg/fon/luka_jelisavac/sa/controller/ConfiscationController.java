package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.FlightViolation;
import rs.ac.bg.fon.luka_jelisavac.sa.service.ConfiscationService;

import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/confiscations")
@RequiredArgsConstructor
public class ConfiscationController {

    private final ConfiscationService confiscationService;

    @PostMapping
    public ResponseEntity<FlightViolation> issue(@RequestBody FlightViolation violation) {
        return new ResponseEntity<>(confiscationService.issueConfiscation(violation), HttpStatus.CREATED);
    }

    @GetMapping("/check/{violationId}")
    public ResponseEntity<Boolean> checkSeizureRequirement(@PathVariable UUID violationId) {
        return ResponseEntity.ok(confiscationService.requiresImmediateSeizure(violationId));
    }

    @PostMapping("/ground/{broomstickId}")
    public ResponseEntity<Void> groundBroom(@PathVariable UUID broomstickId) {
        confiscationService.groundBroomstick(broomstickId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{violationId}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable UUID violationId) {
        confiscationService.resolveViolation(violationId);
        return ResponseEntity.noContent().build();
    }
}

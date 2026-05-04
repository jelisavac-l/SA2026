package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomModel;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.service.BroomModelService;
import rs.ac.bg.fon.luka_jelisavac.sa.service.BroomstickService;

import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */
@RestController
@RequestMapping("/api/broomsticks")
@RequiredArgsConstructor
public class BroomstickController {

    private final BroomstickService broomstickService;
    private final BroomModelService modelService;

    @PostMapping("/model/{modelId}")
    public ResponseEntity<Broomstick> register(@PathVariable UUID modelId, @RequestBody Broomstick broomstick) {
        BroomModel model = modelService.getModelById(modelId);
        broomstick.setModel(model);

        Broomstick created = broomstickService.registerBroomstick(broomstick);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<Broomstick> getBySerial(@PathVariable String serialNumber) {
        return ResponseEntity.ok(broomstickService.findBySerial(serialNumber));
    }

    @PatchMapping("/{id}/condition")
    public ResponseEntity<Broomstick> updateCondition(@PathVariable UUID id, @RequestParam Integer score) {
        Broomstick updated = broomstickService.updateCondition(id, score);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}/isSafe")
    public ResponseEntity<Boolean> checkSafety(@PathVariable UUID id) {
        Boolean isSafe = broomstickService.isSafeForFlight(id);
        return ResponseEntity.ok(isSafe);
    }

}

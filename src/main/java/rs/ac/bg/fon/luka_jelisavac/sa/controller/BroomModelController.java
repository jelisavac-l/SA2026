package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomModel;
import rs.ac.bg.fon.luka_jelisavac.sa.service.BroomModelService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */
@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class BroomModelController {

    private final BroomModelService modelService;

    @PostMapping
    public ResponseEntity<BroomModel> register(@RequestBody BroomModel model) {
        BroomModel created = modelService.registerModel(model);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BroomModel>> getAll() {
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BroomModel> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(modelService.getModelById(id));
    }

    @PutMapping("/{id}/specs")
    public ResponseEntity<BroomModel> updateSpecs(@PathVariable UUID id, @RequestBody BroomModel updatedSpecs) {
        BroomModel updated = modelService.updateModelSpecs(id, updatedSpecs);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> retire(@PathVariable UUID id) {
        modelService.retireModel(id);
        return ResponseEntity.noContent().build();
    }
}

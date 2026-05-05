package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomActivity;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.MaintenanceRecord;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.SafetyInspection;
import rs.ac.bg.fon.luka_jelisavac.sa.service.BroomActivityService;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class BroomActivityController {

    private final BroomActivityService activityService;

    @PostMapping("/broomstick/{broomstickId}/inspection")
    public ResponseEntity<BroomActivity> recordInspection(
        @PathVariable UUID broomstickId,
        @RequestBody SafetyInspection inspection) {
        return new ResponseEntity<>(activityService.processActivity(broomstickId, inspection), HttpStatus.CREATED);
    }

    @PostMapping("/broomstick/{broomstickId}/maintenance")
    public ResponseEntity<BroomActivity> recordMaintenance(
        @PathVariable UUID broomstickId,
        @RequestBody MaintenanceRecord maintenance) {
        return new ResponseEntity<>(activityService.processActivity(broomstickId, maintenance), HttpStatus.CREATED);
    }

    @GetMapping("/broomstick/{broomstickId}")
    public ResponseEntity<List<BroomActivity>> getHistory(@PathVariable UUID broomstickId) {
        return ResponseEntity.ok(activityService.getActivityHistory(broomstickId));
    }
}

package rs.ac.bg.fon.luka_jelisavac.sa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomActivity;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.MaintenanceRecord;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.SafetyInspection;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomActivityRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.repo.BroomstickRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for activities performed on brooms which uses generic pattern so
 * that both maintenance records and safety inspections can be handled.
 * @author Luka Jelisavac (jelisavac-l)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BroomActivityService {

    private final BroomActivityRepository activityRepository;
    private final BroomstickRepository broomstickRepository;

    /**
     * Records a safety inspection or maintenance work for a broom.
     * Must be linked to a physical unit and performed by a staff member.
     * @param broomstickId UUID of the broomstick activity was performed on.
     * @param activity Activity of performed on the broom.
     * @return Saved BroomActivity.
     * @throws EntityNotFoundException If broomstick was not found.
     */
    public BroomActivity processActivity(UUID broomstickId, BroomActivity activity) {
        Broomstick broom = broomstickRepository.findById(broomstickId)
            .orElseThrow(() -> new EntityNotFoundException("Broomstick not found."));

        activity.setBroomstick(broom);
        activity.setDate(LocalDateTime.now());

        if (activity instanceof SafetyInspection si) {
            handleInspectionLogic(broom, si);
        } else if (activity instanceof MaintenanceRecord mr) {
            handleMaintenanceLogic(mr);
        }

        return activityRepository.save(activity);
    }

    /**
     * Handles logic for safety inspection.
     * @param broom Broomstick instance.
     * @param si Safety inspection performed on the given broomstick.
     */
    private void handleInspectionLogic(Broomstick broom, SafetyInspection si) {
        si.setExpirationDate(LocalDateTime.now().plusYears(1));
        broom.setCurrentCondition(si.getStructuralIntegrityScore());
        broomstickRepository.save(broom);
    }

    /**
     * Handles maintenance logic.
     * @param mr Maintenance record.
     */
    private void handleMaintenanceLogic(MaintenanceRecord mr) {
        System.out.println("Maintenance cost recorded: " + mr.getCost());
    }

    /**
     * Retrieves list of activities on the given broomstick.
     * @param broomstickId UUID of the broomstick.
     * @return List of activities performed on the given broomstick.
     */
    @Transactional(readOnly = true)
    public List<BroomActivity> getActivityHistory(UUID broomstickId) {
        return activityRepository.findByBroomstickIdOrderByDateDesc(broomstickId);
    }
}

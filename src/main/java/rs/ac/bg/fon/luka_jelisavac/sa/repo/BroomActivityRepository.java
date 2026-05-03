package rs.ac.bg.fon.luka_jelisavac.sa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomActivity;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

public interface BroomActivityRepository extends JpaRepository<BroomActivity, UUID> {
    List<BroomActivity> findByBroomstickIdOrderByDateDesc(UUID broomstickId);
}

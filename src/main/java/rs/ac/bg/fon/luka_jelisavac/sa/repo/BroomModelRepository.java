package rs.ac.bg.fon.luka_jelisavac.sa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomModel;

import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

public interface BroomModelRepository extends JpaRepository<BroomModel, UUID> {
    boolean existsByModelName(String modelName);
}

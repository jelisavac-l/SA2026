package rs.ac.bg.fon.luka_jelisavac.sa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Ownership;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

public interface OwnershipRepository extends JpaRepository<Ownership, UUID> {
    List<Ownership> findByBroomstickId(UUID broomstickId);
    List<Ownership> findByOwnerIdOrderByStartDateDesc(UUID ownerId);
}

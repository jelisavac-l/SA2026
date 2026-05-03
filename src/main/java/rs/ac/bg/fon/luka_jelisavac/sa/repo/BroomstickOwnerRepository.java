package rs.ac.bg.fon.luka_jelisavac.sa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.BroomstickOwner;

import java.util.List;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

public interface BroomstickOwnerRepository extends JpaRepository<BroomstickOwner, UUID> {
    List<BroomstickOwner> findByLastNameIgnoreCase(String lastName);
}

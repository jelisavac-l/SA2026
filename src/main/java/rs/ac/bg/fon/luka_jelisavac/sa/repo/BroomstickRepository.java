package rs.ac.bg.fon.luka_jelisavac.sa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Broomstick;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

public interface BroomstickRepository extends JpaRepository<Broomstick, UUID> {
    Optional<Broomstick> findBySerialNumber(String serialNumber);
}

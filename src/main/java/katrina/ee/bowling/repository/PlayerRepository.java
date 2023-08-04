package katrina.ee.bowling.repository;

import katrina.ee.bowling.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

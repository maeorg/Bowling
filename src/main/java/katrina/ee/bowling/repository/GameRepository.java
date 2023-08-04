package katrina.ee.bowling.repository;

import katrina.ee.bowling.entity.Game;
import katrina.ee.bowling.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findGamesByPlayersId(Long id);
}

package katrina.ee.bowling.repository;

import katrina.ee.bowling.entity.Frame;
import katrina.ee.bowling.entity.Game;
import katrina.ee.bowling.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrameRepository extends JpaRepository<Frame, Long> {

    List<Frame> findAllByPlayerOrderByGame(Player player);

    List<Frame> findAllByGameOrderById(Game game);

    List<Frame> findFramesByGameAndPlayerOrderById(Game game, Player player);

    Frame findFirstByGameAndPlayerOrderByIdDesc(Game game, Player player);

}

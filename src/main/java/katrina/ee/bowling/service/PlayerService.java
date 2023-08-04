package katrina.ee.bowling.service;

import katrina.ee.bowling.entity.Player;
import katrina.ee.bowling.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public void addPlayer(Player player) {
        playerRepository.save(player);
    }

    public void savePlayers(List<Player> players) {
        playerRepository.saveAll(players);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id).get();
    }
}

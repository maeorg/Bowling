package katrina.ee.bowling.service;

import katrina.ee.bowling.entity.Frame;
import katrina.ee.bowling.entity.Game;
import katrina.ee.bowling.entity.Player;
import katrina.ee.bowling.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public Game getGame(Long id) {
        return gameRepository.findById(id).get();
    }
}

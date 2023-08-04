package katrina.ee.bowling.controller;

import katrina.ee.bowling.entity.Frame;
import katrina.ee.bowling.entity.Game;
import katrina.ee.bowling.entity.Player;
import katrina.ee.bowling.repository.FrameRepository;
import katrina.ee.bowling.repository.GameRepository;
import katrina.ee.bowling.service.FrameService;
import katrina.ee.bowling.service.GameService;
import katrina.ee.bowling.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bowling/game")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    FrameService frameService;

    @GetMapping
    public ResponseEntity getAllGames() {
        return new ResponseEntity(gameRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("scoreBoard")
    public ResponseEntity getScoreBoardForPlayer(
            @RequestParam Long playerId, @RequestParam(required = false) Long gameId) {
        List<Frame> foundFrames;
        Map<String, Map> scoreBoard = new HashMap<>();
        if (gameId == null) {
            foundFrames = frameRepository.findAllByPlayerOrderByGame(playerService.getPlayer(playerId));
        } else {
            foundFrames = frameRepository.findFramesByGameAndPlayerOrderById(
                    gameService.getGame(gameId), playerService.getPlayer(playerId));
        }
        return new ResponseEntity(foundFrames, HttpStatus.OK);
    }

    @GetMapping("runningTotals/{playerId}") // get running totals for games by playerId
    public ResponseEntity getGameScoresForPlayer(@PathVariable Long playerId) {
        List<Game> games = gameRepository.findGamesByPlayersId(playerId);
        Map<Long, Integer> gameScores = new HashMap<>();
        for (Game game : games) {
            Frame foundFrame = frameRepository.findFirstByGameAndPlayerOrderByIdDesc(
                    gameService.getGame(game.getId()), playerService.getPlayer(playerId));
            gameScores.put(game.getId(), foundFrame.getRunningTotal());
        }
        return new ResponseEntity(gameScores, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity startNewGame(@RequestBody List<Player> players) throws Exception {
        Game game = gameRepository.save(new Game());
        List<Player> foundPlayers = new ArrayList<>();
        for (Player player : players) {
            foundPlayers.add(playerService.getPlayer(player.getId()));
            for (int i = 0; i < 10; i++) {
                frameService.addFrame(game.getId(), player.getId());
            }
        }
        game.setPlayers(foundPlayers);
        gameRepository.save(game);
        return new ResponseEntity(gameRepository.findById(game.getId()), HttpStatus.CREATED);
    }
}

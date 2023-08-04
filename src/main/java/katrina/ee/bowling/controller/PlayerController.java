package katrina.ee.bowling.controller;

import katrina.ee.bowling.entity.Player;
import katrina.ee.bowling.repository.PlayerRepository;
import katrina.ee.bowling.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bowling/player")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @GetMapping
    public ResponseEntity getPlayers() {
        return new ResponseEntity(playerRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addPlayer(@Validated @RequestBody Player player) {
        playerService.addPlayer(player);
        return new ResponseEntity(playerRepository.findAll(), HttpStatus.CREATED);
    }
}

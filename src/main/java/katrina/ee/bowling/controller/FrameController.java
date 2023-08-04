package katrina.ee.bowling.controller;

import katrina.ee.bowling.entity.Frame;
import katrina.ee.bowling.repository.FrameRepository;
import katrina.ee.bowling.service.FrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bowling/frame")
public class FrameController {

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    FrameService frameService;

    @GetMapping
    public ResponseEntity getAllFrames() {
        return new ResponseEntity(frameRepository.findAll(), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity addFrame(@RequestParam Long gameId, @RequestParam Long playerId) throws Exception {
//        frameService.addFrame(gameId, playerId);
//        return new ResponseEntity(frameRepository.findAll(), HttpStatus.CREATED);
//    }

    @PatchMapping("{frameId}")
    public ResponseEntity addRollToFrame(@PathVariable Long frameId,
                                         @RequestParam(required = false) Integer firstRoll,
                                         @RequestParam(required = false) Integer secondRoll,
                                         @RequestParam(required = false) Integer thirdRoll) {
        frameService.addRollToFrame(frameId, firstRoll, secondRoll, thirdRoll);
        return new ResponseEntity(frameRepository.findById(frameId), HttpStatus.OK);
    }

}

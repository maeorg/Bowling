package katrina.ee.bowling.service;

import katrina.ee.bowling.entity.Frame;
import katrina.ee.bowling.entity.Game;
import katrina.ee.bowling.entity.Player;
import katrina.ee.bowling.repository.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameService {

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    GameService gameService;

    public void addFrame(Long gameId, Long playerId) throws Exception {
        Player playerFound = playerService.getPlayer(playerId);
        Game gameFound = gameService.getGame(gameId);
        List<Frame> foundFrames = frameRepository.findFramesByGameAndPlayerOrderById(gameFound, playerFound);
        int orderNr = foundFrames.size()+1;
        if (orderNr > 10) {
            throw new Exception("This game and player already has 10 frames. Can't add any more.");
        }
        Frame frame = Frame.builder().orderNr(orderNr).player(playerFound).game(gameFound).build();
        frameRepository.save(frame);
    }

    public void addRollToFrame(Long frameId, Integer firstRoll, Integer secondRoll, Integer thirdRoll) {
        Frame frame = frameRepository.findById(frameId).get();
        if (firstRoll != null
                && firstRoll <= 10 && firstRoll >= 0) {
            frame.setFirstRoll(firstRoll);
        }
        if (frame.getFirstRoll() != null && secondRoll != null
                && secondRoll <= 10 && secondRoll >= 0
                && (frame.getFirstRoll() + secondRoll <= 10 || frame.getOrderNr() == 10)) {
            if ((frame.getOrderNr() < 10 && frame.getFirstRoll() < 10) || frame.getOrderNr() == 10) {
                frame.setSecondRoll(secondRoll);
            }
        }
        if (frame.getFirstRoll() != null && frame.getSecondRoll() != null && thirdRoll != null
                && thirdRoll <= 10 && thirdRoll >= 0
                && frame.getOrderNr().equals(10) &&
                ((frame.getFirstRoll() + frame.getSecondRoll()) >= 10 )) {
            frame.setThirdRoll(thirdRoll);
        }
        frameRepository.save(frame);
        calculateScore(frame);
    }

    private void calculateScore(Frame frame) {
        List<Frame> foundFrames = frameRepository.findFramesByGameAndPlayerOrderById(frame.getGame(), frame.getPlayer());

        int runningTotal = 0;

        for (int i = 0; i < foundFrames.size(); i++) {
            Frame currentFrame = foundFrames.get(i);

            // if it's an open frame
            if (currentFrame.getFirstRoll() != null && currentFrame.getSecondRoll() != null
                    && currentFrame.getFirstRoll() + currentFrame.getSecondRoll() < 10) {
                currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
            }

            // if it's a spare and the tenth round
            if (currentFrame.getFirstRoll() != null && currentFrame.getSecondRoll() != null
                    && currentFrame.getFirstRoll() + currentFrame.getSecondRoll() == 10
                    && currentFrame.getOrderNr() == 10) {
                currentFrame.setScore(10 + currentFrame.getThirdRoll());
            }

            // if it's not the tenth round
            if (i < foundFrames.size()-1) {
                Frame nextFrame = foundFrames.get(i+1);

                // and if it's a spare
                if (currentFrame.getFirstRoll() != null && currentFrame.getSecondRoll() != null
                        && nextFrame.getFirstRoll() != null
                        && currentFrame.getFirstRoll() + currentFrame.getSecondRoll() == 10) {
                    currentFrame.setScore(10 + nextFrame.getFirstRoll());
                } else if (currentFrame.getFirstRoll() != null && currentFrame.getFirstRoll() == 10) { // if it's a strike
                    if (nextFrame.getFirstRoll() != null && nextFrame.getFirstRoll() == 10) {
                        if (i < foundFrames.size()-2 && foundFrames.get(i+2).getFirstRoll() != null) {
                            currentFrame.setScore(20 + foundFrames.get(i+2).getFirstRoll());
                        } else if (nextFrame.getSecondRoll() != null) {
                            currentFrame.setScore(20 + nextFrame.getSecondRoll());
                        }
                    } else if (nextFrame.getFirstRoll() != null && nextFrame.getSecondRoll() != null
                            && nextFrame.getFirstRoll() < 10) {
                        currentFrame.setScore(10 + nextFrame.getFirstRoll() + nextFrame.getSecondRoll());
                    }
                }
            } else if (currentFrame.getOrderNr() == 10
                    && currentFrame.getFirstRoll() != null  && currentFrame.getSecondRoll() != null
                    && currentFrame.getThirdRoll() != null ) {
                currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll() + currentFrame.getThirdRoll());
            }

            // calculate running total
            if (currentFrame.getScore() != null) {
                runningTotal += currentFrame.getScore();
            }
            currentFrame.setRunningTotal(runningTotal);

            frameRepository.save(currentFrame);
        }
    }
}

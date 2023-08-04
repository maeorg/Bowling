package katrina.ee.bowling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Frame {

    @Id
    @GeneratedValue
    private Long id;
    private Integer orderNr;
    private Integer firstRoll;
    private Integer secondRoll;
    private Integer thirdRoll;
    private Integer score;
    private Integer runningTotal;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Game game;
}

package arctic.ac.manager;

import arctic.ac.check.Check;
import arctic.ac.check.impl.combat.aim.AimA;
import arctic.ac.check.impl.combat.aim.AimB;
import arctic.ac.check.impl.movement.fly.FlyA;
import arctic.ac.check.impl.movement.fly.FlyB;
import arctic.ac.check.impl.movement.fly.FlyC;
import arctic.ac.check.impl.movement.speed.SpeedA;
import arctic.ac.data.PlayerData;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CheckManager {

    private final PlayerData data;
    private final List<Check> checks;


    public CheckManager(PlayerData data) {
        this.data = data;
        checks = Arrays.asList(
                new FlyA(data),
                new FlyB(data),
                new FlyC(data),
                new SpeedA(data),

                new AimA(data),
                new AimB(data)
        );
    }
}

package honeybadger.ac.data.impl;

import honeybadger.ac.check.Check;
import honeybadger.ac.check.checks.movement.fly.FlyA;
import honeybadger.ac.check.checks.movement.speed.SpeedA;
import honeybadger.ac.data.PlayerData;

import java.util.Arrays;
import java.util.List;

public class CheckManager {

    private final PlayerData data;

    private List<Check> checks;

    public CheckManager(PlayerData data) {
        this.data = data;

        this.checks = Arrays.asList(

                //MOVEMENT
                new FlyA(data),
                new SpeedA(data)

        );
    }


    public List<Check> getChecks() {
        return checks;
    }
}

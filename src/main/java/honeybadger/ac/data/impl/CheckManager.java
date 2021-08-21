package honeybadger.ac.data.impl;

import honeybadger.ac.check.Check;
import honeybadger.ac.check.checks.combat.autoclicker.AutoclickerA;
import honeybadger.ac.check.checks.combat.killaura.KillAuraA;
import honeybadger.ac.check.checks.combat.killaura.KillAuraB;
import honeybadger.ac.check.checks.combat.killaura.KillAuraC;
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
                new SpeedA(data),


                // COMBAT
                new AutoclickerA(data),
                new KillAuraA(data),
                new KillAuraB(data),
                new KillAuraC(data)
        );
    }


    public List<Check> getChecks() {
        return checks;
    }
}

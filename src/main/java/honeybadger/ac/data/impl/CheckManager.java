package honeybadger.ac.data.impl;

import honeybadger.ac.check.Check;
import honeybadger.ac.check.checks.combat.aim.AimA;
import honeybadger.ac.check.checks.combat.aim.AimB;
import honeybadger.ac.check.checks.combat.aim.AimC;
import honeybadger.ac.check.checks.combat.aim.AimD;
import honeybadger.ac.check.checks.combat.autoclicker.AutoclickerA;
import honeybadger.ac.check.checks.combat.entity.EntityA;
import honeybadger.ac.check.checks.combat.killaura.*;
import honeybadger.ac.check.checks.combat.reach.Reach;
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
                new AimA(data),
                new AimB(data),
                new AimC(data),
                new AimD(data),
                new EntityA(data),
                new AutoclickerA(data),
                new KillAuraA(data),
                new KillAuraB(data),
                new KillAuraC(data),
                new KillAuraD(data),
                new KillAuraE(data),
                new Reach(data)
        );
    }


    public List<Check> getChecks() {
        return checks;
    }
}

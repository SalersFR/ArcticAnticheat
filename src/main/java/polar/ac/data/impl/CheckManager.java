package polar.ac.data.impl;

import polar.ac.check.Check;
import polar.ac.check.checks.combat.aim.AimA;
import polar.ac.check.checks.combat.aim.AimB;
import polar.ac.check.checks.combat.aim.AimC;
import polar.ac.check.checks.combat.aim.AimD;
import polar.ac.check.checks.combat.autoclicker.AutoclickerA;
import polar.ac.check.checks.combat.autoclicker.AutoclickerB;
import polar.ac.check.checks.combat.entity.EntityA;
import polar.ac.check.checks.combat.killaura.*;
import polar.ac.check.checks.combat.reach.Reach;
import polar.ac.check.checks.movement.fly.FlyA;
import polar.ac.check.checks.movement.fly.FlyB;
import polar.ac.check.checks.movement.fly.FlyC;
import polar.ac.check.checks.movement.motion.MotionB;
import polar.ac.check.checks.movement.nofall.NoFallA;
import polar.ac.check.checks.movement.speed.SpeedA;
import polar.ac.check.checks.movement.step.StepA;
import polar.ac.data.PlayerData;

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
                new FlyB(data),
                new FlyC(data),
                new SpeedA(data),
                new StepA(data),
                new NoFallA(data),
                new MotionB(data),


                // COMBAT
                new AimA(data),
                new AimB(data),
                new AimC(data),
                new AimD(data),
                new EntityA(data),
                new AutoclickerA(data),
                new AutoclickerB(data),
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

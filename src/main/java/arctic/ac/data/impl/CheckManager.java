package arctic.ac.data.impl;

import arctic.ac.check.Check;
import arctic.ac.check.checks.combat.aim.*;
import arctic.ac.check.checks.combat.autoclicker.AutoclickerA;
import arctic.ac.check.checks.combat.autoclicker.AutoclickerB;
import arctic.ac.check.checks.combat.autoclicker.AutoclickerC;
import arctic.ac.check.checks.combat.entity.EntityA;
import arctic.ac.check.checks.combat.killaura.*;
import arctic.ac.check.checks.combat.reach.Reach;
import arctic.ac.check.checks.combat.velocity.VelocityA;
import arctic.ac.check.checks.movement.fly.FlyA;
import arctic.ac.check.checks.movement.fly.FlyB;
import arctic.ac.check.checks.movement.fly.FlyC;
import arctic.ac.check.checks.movement.motion.MotionA;
import arctic.ac.check.checks.movement.motion.MotionB;
import arctic.ac.check.checks.movement.nofall.NoFallA;
import arctic.ac.check.checks.movement.nofall.NoFallB;
import arctic.ac.check.checks.movement.speed.SpeedA;
import arctic.ac.check.checks.movement.step.StepA;
import arctic.ac.check.checks.player.badpackets.BadPacketsA;
import arctic.ac.check.checks.player.badpackets.BadPacketsB;
import arctic.ac.check.checks.player.scaffold.ScaffoldA;
import arctic.ac.check.checks.player.timer.TimerA;
import arctic.ac.data.PlayerData;

import java.util.Arrays;
import java.util.List;

public class CheckManager {

    private static List<Check> checks;
    private final PlayerData data;

    public CheckManager(PlayerData data) {
        this.data = data;

        this.checks = Arrays.asList(

                //PLAYER
                new BadPacketsA(data),
                new BadPacketsB(data),

                new TimerA(data),

                new ScaffoldA(data),

                //MOVEMENT
                new FlyA(data),
                new FlyB(data),
                new FlyC(data),

                new SpeedA(data),

                new StepA(data),

                new NoFallA(data),
                new NoFallB(data),

                new MotionA(data),
                new MotionB(data),


                // COMBAT
                new AimA(data),
                new AimB(data),
                new AimC(data),
                new AimD(data),
                new AimE(data),
                new AimF(data),
                new AimG(data),
                new AimH(data),
                new AimI(data),

                new EntityA(data),

                new AutoclickerA(data),
                new AutoclickerB(data),
                new AutoclickerC(data),

                new KillAuraA(data),
                new KillAuraB(data),
                new KillAuraC(data),
                new KillAuraD(data),
                new KillAuraE(data),
                new KillAuraF(data),
                new KillAuraG(data),
                new KillAuraH(data),
                new KillAuraI(data),

                new VelocityA(data),

                new Reach(data)

        );
    }
    // inefficient


    public static List<Check> getChecks() {
        return checks;
    }
}
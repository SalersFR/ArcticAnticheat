package arctic.ac.data.impl;

import arctic.ac.check.Check;
import arctic.ac.check.checks.combat.aim.*;
import arctic.ac.check.checks.combat.autoclicker.*;
import arctic.ac.check.checks.combat.killaura.*;
import arctic.ac.check.checks.combat.reach.Reach;
import arctic.ac.check.checks.combat.reach.ReachB;
import arctic.ac.check.checks.combat.velocity.VelocityA;
import arctic.ac.check.checks.combat.velocity.VelocityB;
import arctic.ac.check.checks.combat.velocity.VelocityC;
import arctic.ac.check.checks.movement.fly.FlyA;
import arctic.ac.check.checks.movement.fly.FlyB;
import arctic.ac.check.checks.movement.fly.FlyC;
import arctic.ac.check.checks.movement.fly.FlyD;
import arctic.ac.check.checks.movement.motion.*;
import arctic.ac.check.checks.movement.nofall.NoFallA;
import arctic.ac.check.checks.movement.nofall.NoFallB;
import arctic.ac.check.checks.movement.nofall.NoFallC;
import arctic.ac.check.checks.movement.predictions.PredictionA;
import arctic.ac.check.checks.movement.speed.SpeedA;
import arctic.ac.check.checks.movement.speed.SpeedB;
import arctic.ac.check.checks.movement.speed.SpeedC;
import arctic.ac.check.checks.movement.speed.SpeedD;
import arctic.ac.check.checks.movement.spider.SpiderA;
import arctic.ac.check.checks.movement.step.StepA;
import arctic.ac.check.checks.player.badpackets.*;
import arctic.ac.check.checks.player.scaffold.ScaffoldA;
import arctic.ac.check.checks.player.timer.TimerA;
import arctic.ac.check.checks.player.timer.TimerB;
import arctic.ac.data.PlayerData;

import java.util.Arrays;
import java.util.List;

public class CheckManager {

    private final PlayerData data;
    private List<Check> checks;

    public CheckManager(PlayerData data) {
        this.data = data;

        this.checks = Arrays.asList(

                //PLAYER
                new BadPacketsA(data),
                new BadPacketsB(data),
                new BadPacketsC(data),
                new BadPacketsD(data),
                new BadPacketsE(data),
                new BadPacketsF(data),


                new TimerA(data),
                new TimerB(data),

                new ScaffoldA(data),

                //MOVEMENT
                new FlyA(data),
                new FlyB(data),
                new FlyC(data),
                new FlyD(data),

                new SpiderA(data),

                new SpeedA(data),
                new SpeedB(data),
                new SpeedC(data),
                new SpeedD(data),
                new PredictionA(data),

                new StepA(data),

                new NoFallA(data),
                new NoFallB(data),
                new NoFallC(data),

                new MotionA(data),
                new MotionB(data),
                new MotionC(data),
                new MotionD(data),
                new MotionE(data),
                new MotionF(data),
                new MotionG(data),

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
                new AimJ(data),
                new AimK(data),
                new AimL(data),
                new AimM(data),
                new AimN(data),
                new AimO(data),
                new AimP(data),
                new AimQ(data),
                new AimR(data),
                new AimS(data),
                new AimT(data),
                new AimU(data),

                new AutoclickerA(data),
                new AutoclickerB(data),
                new AutoclickerC(data),
                new AutoclickerD(data),
                new AutoclickerE(data),
                new AutoclickerF(data),
                new AutoclickerG(data),
                new AutoclickerH(data),

                new KillAuraA(data),
                new KillAuraB(data),
                new KillAuraC(data),
                new KillAuraD(data),
                new KillAuraE(data),
                new KillAuraF(data),
                new KillAuraG(data),
                new KillAuraH(data),
                new KillAuraI(data),
                new KillAuraJ(data),

                new VelocityA(data),
                new VelocityB(data),
                new VelocityC(data),

                new Reach(data),
                new ReachB(data)

        );
    }
    // inefficient


    public List<Check> getChecks() {
        return this.checks;
    }
}

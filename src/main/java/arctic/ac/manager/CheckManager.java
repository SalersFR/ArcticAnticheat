package arctic.ac.manager;

import arctic.ac.check.Check;
import arctic.ac.check.impl.combat.aim.*;
import arctic.ac.check.impl.combat.autoclicker.*;
import arctic.ac.check.impl.combat.killaura.KillAuraA;
import arctic.ac.check.impl.combat.killaura.KillAuraB;
import arctic.ac.check.impl.movement.fly.FlyA;
import arctic.ac.check.impl.movement.fly.FlyB;
import arctic.ac.check.impl.movement.fly.FlyC;
import arctic.ac.check.impl.movement.motion.MotionA;
import arctic.ac.check.impl.movement.motion.MotionB;
import arctic.ac.check.impl.movement.motion.MotionC;
import arctic.ac.check.impl.movement.speed.SpeedA;
import arctic.ac.check.impl.player.timer.TimerA;
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

                new TimerA(data),

                new FlyA(data),
                new FlyB(data),
                new FlyC(data),
                new SpeedA(data),
                new MotionA(data),
                new MotionB(data),
                new MotionC(data),


                new KillAuraA(data),
                new KillAuraB(data),
                new AutoclickerA(data),
                new AutoclickerA1(data),
                new AutoclickerB(data),
                new AutoclickerC(data),
                new AutoclickerD(data),
                new AutoclickerE(data),
                new AimA(data),
                new AimA1(data),
                new AimB(data),
                new AimC(data),
                new AimC1(data),
                new AimD(data),
                new AimE(data),
                new AimE1(data),
                new AimF(data),
                new AimF1(data),
                new AimG(data),
                new AimG1(data),
                new AimH(data),
                new AimI(data),
                new AimJ(data),
                new AimJ1(data)
        );
    }
}

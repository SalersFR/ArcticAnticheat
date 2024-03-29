package dev.arctic.anticheat.manager;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.check.impl.combat.aim.*;
import dev.arctic.anticheat.check.impl.combat.autoclicker.*;
import dev.arctic.anticheat.check.impl.combat.killaura.*;
import dev.arctic.anticheat.check.impl.combat.reach.ReachA;
import dev.arctic.anticheat.check.impl.combat.velocity.VelocityA;
import dev.arctic.anticheat.check.impl.combat.velocity.VelocityB;
import dev.arctic.anticheat.check.impl.movement.flight.FlightA;
import dev.arctic.anticheat.check.impl.movement.flight.FlightA1;
import dev.arctic.anticheat.check.impl.movement.flight.FlightB;
import dev.arctic.anticheat.check.impl.movement.flight.FlightC;
import dev.arctic.anticheat.check.impl.movement.motion.*;
import dev.arctic.anticheat.check.impl.movement.speed.SpeedA;
import dev.arctic.anticheat.check.impl.movement.speed.SpeedB;
import dev.arctic.anticheat.check.impl.movement.speed.SpeedC;
import dev.arctic.anticheat.check.impl.movement.step.StepA;
import dev.arctic.anticheat.check.impl.movement.step.StepA1;
import dev.arctic.anticheat.check.impl.player.badpackets.*;
import dev.arctic.anticheat.check.impl.player.scaffold.*;
import dev.arctic.anticheat.check.impl.player.ground.*;
import dev.arctic.anticheat.check.impl.player.timer.TimerA;
import dev.arctic.anticheat.data.PlayerData;

import java.util.Arrays;
import java.util.List;

public class CheckManager {
    private final List<Check> checks;

    public CheckManager(final PlayerData playerData) {
        checks = Arrays.asList(

                new ScaffoldA(playerData),
                new ScaffoldA1(playerData),
                new ScaffoldB(playerData),
                new ScaffoldC(playerData),
                new ScaffoldD(playerData),
                new ScaffoldE(playerData),
                new BadPacketsA(playerData),
                new BadPacketsB(playerData),
                new BadPacketsC(playerData),
                new BadPacketsD(playerData),
                new BadPacketsE(playerData),
                new BadPacketsF(playerData),
                new BadPacketsG(playerData),
                new TimerA(playerData),
                new GroundA(playerData),
                new GroundB(playerData),

                new FlightA(playerData),
                new FlightB(playerData),
                new FlightA1(playerData),
                new FlightC(playerData),
                new SpeedA(playerData),
                new SpeedB(playerData),
                new SpeedC(playerData),
                new MotionA(playerData),
                new MotionB(playerData),
                new MotionC(playerData),
                new MotionD(playerData),
                new MotionE(playerData),
                new MotionF(playerData),
                new MotionG(playerData),
                new StepA(playerData),
                new StepA1(playerData),


                new VelocityA(playerData),
                new VelocityB(playerData),
                new KillAuraA(playerData),
                new KillAuraB(playerData),
                new KillAuraC(playerData),
                new KillAuraD(playerData),
                new KillAuraE(playerData),
                new KillAuraF(playerData),
                new KillAuraG(playerData),
                new KillAuraH(playerData),
                new KillAuraI(playerData),
                new KillAuraI1(playerData),
                new KillAuraI2(playerData),
//                new KillAuraJ(playerData),
                new AutoclickerA(playerData),
                new AutoclickerA1(playerData),
                new AutoclickerB(playerData),
                new AutoclickerC(playerData),
                new AutoclickerD(playerData),
                new AutoclickerE(playerData),
                new AutoclickerF(playerData),
                new AutoclickerG(playerData),
                new AimA(playerData),
                new AimA1(playerData),
                new AimA2(playerData),
                new AimB(playerData),
                new AimC(playerData),
                new AimC1(playerData),
                new AimD(playerData),
                new AimE(playerData),
                new AimE1(playerData),
                new AimF(playerData),
                new AimF1(playerData),
                new AimF2(playerData),
                new AimF3(playerData),
                new AimG(playerData),
                new AimG1(playerData),
                new AimH(playerData),
                new AimI(playerData),
                new AimJ(playerData),
                new AimJ1(playerData),
                new AimK(playerData),
                new AimK1(playerData),
                new AimL(playerData),
                new ReachA(playerData)

        );

    }

    public List<Check> getChecks() {
        return checks;
    }
}

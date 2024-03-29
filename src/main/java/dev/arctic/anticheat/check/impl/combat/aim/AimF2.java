package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

import java.util.ArrayList;

/**
 * @author xWand
 */
public class AimF2 extends Check {

    private double lastMode, lastLastMode;

    private ArrayList<Double> samples = new ArrayList<>();

    /*
    TODO
    fix this check innit
     */

    public AimF2(PlayerData data) {
        super(data, "Aim", "F2", "combat.aim.f2", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            RotationProcessor rot = data.getRotationProcessor();
            double deltaYaw = rot.getDeltaYaw();
            double deltaPitch = rot.getDeltaPitch();

            double smaller = Math.min(deltaYaw, deltaPitch);
            double toBeDivided = Math.max(deltaYaw, deltaPitch);
            double divisor = Math.round(toBeDivided / smaller);
            double euclidean = toBeDivided % divisor;

            double length = String.valueOf(euclidean).length();
            if (deltaYaw > 2.25f)
                samples.add(length);


            if (samples.size() >= 20) {
                double mode = MathUtils.getMode(samples);
                double lastMode = this.lastMode;
                this.lastMode = mode;
                double lastLastMode = this.lastLastMode;
                this.lastLastMode = lastMode;

                debug("mode=" + mode + " last=" + lastMode + " lastLast=" + lastLastMode);

                if (mode == lastMode && lastMode == lastLastMode) {
                   // fail("Consistent mode of rotations. ∆M=" + mode);
                }

                samples.clear();
            }

            /*
            Euclidean Algorithm

            e.g
            Where q = how many times 10 goes into 45.
            Where r = remainder.

            45 = 10 * q + r
            45 = 10 * 4 + 5
            10 = 5 * 2 + 0

            Go back when r = 0, 5 is GCD.
             */
        }
    }
}

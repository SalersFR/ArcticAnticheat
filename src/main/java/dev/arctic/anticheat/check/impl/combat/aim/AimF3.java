package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

/**
 * @author xWand
 */
public class AimF3 extends Check {

    private long lastGood;
    private double lastGcdYaw, lastGcdPitch;

    /*
    TODO
    might need a bit of fixing, works very well against LB aura and aimbot rn
     */

    public AimF3(PlayerData data) {
        super(data, "Aim", "F3", "combat.aim.f3", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            RotationProcessor rot = data.getRotationProcessor();

            final double deltaYaw = rot.getDeltaYaw();
            final double deltaPitch = rot.getDeltaYaw();
            final double lastDeltaYaw = rot.getLastDeltaYaw();
            final double lastDeltaPitch = rot.getLastDeltaPitch();

            final double gcdYaw = MathUtils.getGcd(deltaYaw, lastDeltaYaw);
            final double gcdPitch = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

            if (gcdYaw == gcdPitch) {
                lastGood = System.currentTimeMillis();
            }

            final boolean exempt = (gcdYaw == 0.054290771484375
                    && gcdPitch == 0.054290771484375)
                    || gcdYaw < 0.04;

            final long diff = System.currentTimeMillis() - lastGood;

            //     lol memez 69
            if (diff > 6990 && !exempt && rot.getYawAccel() <= 5 && rot.getDeltaPitch() <= 6.75F) {
                if (++buffer > 14.0) {
                    fail("deltaYaw=" + rot.getDeltaYaw() + ", diff=" + diff + ", " + gcdYaw + "\nbuffer=" + buffer);
                }
            } else if (buffer > 0) buffer -= 0.1;

            /*
            double lastGcdYaw = this.lastGcdYaw;
            this.lastGcdYaw = gcdYaw;
            double lastGcdPitch = this.lastGcdPitch;
            this.lastGcdPitch = gcdPitch;

            double deltaGcdYaw = Math.abs(lastGcdYaw - gcdYaw);
            double deltaGcdPitch = Math.abs(lastGcdPitch - gcdPitch);

            double diff = deltaGcdPitch - deltaGcdYaw;


            if (diff <= 1.0E-5 && !(diff <= 0)) {
                if (++buffer > 3) {
                    fail("gcdDifference=" + diff);
                }
            } else if (buffer > 0) buffer -= 0.05;
             */
        }
    }
}

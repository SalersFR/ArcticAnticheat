package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

public class AimF1 extends Check {

    public AimF1(PlayerData data) {
        super(data, "Aim", "F1", "combat.aim.f1", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {

            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final float deltaPitch = rotationProcessor.getDeltaPitch();
            final float deltaYaw = rotationProcessor.getDeltaYaw();

            final float gcd = (float) rotationProcessor.getGcdPitch();

            final boolean check = (Math.abs(deltaPitch) > 0.45 || Math.abs(deltaYaw) > 0.45)
                    && rotationProcessor.getTicksSinceCinematic() <= 5 && deltaPitch < 15 && deltaYaw < 16;

            final double resYaw = deltaYaw % gcd;
            final double resPitch = deltaPitch % gcd;

            final double modulusResPitch = MathUtils.getReversedModulus(gcd, deltaPitch, resPitch);
            final double modulusResYaw = MathUtils.getReversedModulus(gcd, deltaYaw, resYaw);


            if ((Double.isNaN(modulusResPitch) || Double.isNaN(modulusResYaw)) && check) {
                if (++buffer > 13) {
                    fail("modPitch: " + modulusResPitch + ", modYaw: " + modulusResYaw +
                            ", dYaw: " + deltaYaw + ", dPitch: " + deltaPitch);
                }
            } else if (buffer > 0) buffer -= 0.5D;

            debug("modPitch: " + modulusResPitch + ", modYaw: " + modulusResYaw +
                    ", dYaw: " + deltaYaw + ", dPitch: " + deltaPitch + " b=" + buffer);
        }


    }
}

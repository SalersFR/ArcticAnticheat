package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimG1 extends Check {

    public AimG1(PlayerData data) {
        super(data, "Aim", "G1", "combat.aim.g1", "Checks for too small gcd.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double deltaYaw = rotationProcessor.getDeltaYaw();

            final double gcd = rotationProcessor.getAbsGcdPitch();

            final boolean check =  ((deltaYaw > 1.1 && deltaPitch != 0.0f) ||
                    deltaPitch > 0.7f) && (deltaYaw < 15 && deltaPitch < 15) && rotationProcessor.getTicksSinceCinematic() > 0;


            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + !check + " buffer=" + buffer);

            if (gcd == 0.0 && check) {
                if (++buffer > 10) {
                    fail("gcd=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.125D;
        }


    }
}

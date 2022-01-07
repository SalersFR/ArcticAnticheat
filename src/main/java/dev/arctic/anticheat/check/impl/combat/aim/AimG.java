package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimG extends Check {

    public AimG(PlayerData data) {
        super(data, "Aim", "G", "combat.aim.g", "Checks for invalid & very small gcd.", true);
    }
    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {

            final RotationProcessor rotationProcessor = data.getRotationProcessor();
            final double gcd = rotationProcessor.getAbsGcdPitch();

            if (gcd < 0.01 && gcd != 0 && (rotationProcessor.getDeltaPitch() < 15 && rotationProcessor.getDeltaYaw() < 15)
                    && rotationProcessor.getTicksSinceCinematic() > 21) {
                if (++buffer > 10) {
                    fail("GCD=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.5;


        }

    }
}

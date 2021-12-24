package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimF extends Check {

    public AimF(PlayerData data) {
        super(data, "Aim", "F", "combat.aim.f", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double deltaYaw = rotationProcessor.getDeltaYaw();

            final double gcd = rotationProcessor.getGcdPitch();
            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 7.2 || deltaPitch == 0.0f || rotationProcessor.getTicksSinceCinematic() <= 7;


            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + exempt + " buffer=" + buffer);

            if (gcd <= 0.0 && !exempt) {
                if (++buffer > 11) {
                    fail("gcd=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.075;
        }


    }

}

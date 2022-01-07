package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimB extends Check {

    public AimB(PlayerData data) {
        super(data, "Aim", "B", "combat.aim.b", "Checks for consistent rotations (gcd).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final double deltaPitch = rotationProcessor.getDeltaPitch();

            final double lastDeltaYaw = rotationProcessor.getLastDeltaYaw();

            final double consist = Math.abs(rotationProcessor.getGcdYaw() - rotationProcessor.getGcdPitch());

            if(rotationProcessor.getTicksSinceCinematic() <= 8) return;



            debug("consist=" + consist + " buffer=" + buffer);

            if (consist < 0.005 && (deltaYaw > 2.75f || (deltaPitch != 0.0f && deltaYaw > 1.25f)) &&
                    !Double.toString(consist).contains("E") && Math.abs(rotationProcessor.getPitch()) != 90 &&
                    Math.abs(rotationProcessor.getPitch()) != 90 && (deltaYaw < 65 && lastDeltaYaw < 62.5) && deltaPitch < 20) {

                buffer += (0.25 + (consist * 50f));
                if (consist == 0.0f || data.getRotationProcessor().getTicksSinceCinematic() <= 2) buffer *= 0.2f;

                if (buffer > 4.75)
                    fail("const=" + consist);
            } else if (buffer > 0) buffer -= 0.00225;
        }
    }
}

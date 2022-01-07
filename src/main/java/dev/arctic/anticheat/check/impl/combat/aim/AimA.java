package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimA extends Check {

    public AimA(PlayerData data) {
        super(data, "Aim", "A", "combat.aim.a", "Checks for invalid sensitivity.", true );
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean exempt = rotationProcessor.getDeltaYaw() < 3.2F || rotationProcessor.getDeltaYaw() > 67.25f;
            final int sensitivity = rotationProcessor.getCalcSensitivity();

            debug("sens=" + sensitivity + " exempt=" + exempt + " buffer=" + buffer);

            if (!exempt && sensitivity >= 405 && rotationProcessor.getYawAccel() <= 5 && rotationProcessor.getDeltaPitch() <= 6.75F) {
                if (++buffer > 10) {
                    fail("buffer=" + buffer);
                }
            } else if (buffer > 0) buffer -= 0.065;
        }

    }
}

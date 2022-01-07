package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimA1 extends Check {

    private int lastSensitivity;

    public AimA1(PlayerData data) {
        super(data, "Aim", "A1", "combat.aim.a1", "Checks for invalid sensitivity.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean exempt = rotationProcessor.getDeltaYaw() < 3.2F || rotationProcessor.getDeltaYaw() > 67.25f;
            final int sensitivity = rotationProcessor.getCalcSensitivity();

            debug("sens=" + rotationProcessor.getSensitivity() + " exempt=" + exempt + " buffer=" + buffer);

            if (!exempt && sensitivity >= 125 && rotationProcessor.getYawAccel() <= 5 &&
                    rotationProcessor.getDeltaPitch() <= 6.75F && sensitivity == lastSensitivity) {
                if (++buffer > 16) {
                    fail("buffer=" + buffer);
                }
            } else if (buffer > 0) buffer -= 0.2;

            this.lastSensitivity = sensitivity;
        }

    }
}

package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimC1 extends Check {

    public AimC1(PlayerData data) {
        super(data, "Aim", "C1", "combat.aim.c1", "Checks for smooth rotations (pitch).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double accelPitch = rotationProcessor.getPitchAccel();

            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || rotationProcessor.getDeltaYaw() < 9.5D;

            if(accelPitch <= 0.001 && !exempt) {
                if(++buffer > 7)
                    fail("diff=" + accelPitch);
            } else if(buffer > 0) buffer -= 0.1025;

            debug("acc=" + accelPitch + " buffer=" + buffer);
        }

    }
}
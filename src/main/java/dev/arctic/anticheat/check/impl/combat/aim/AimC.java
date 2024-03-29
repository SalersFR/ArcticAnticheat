package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimC extends Check {

    public AimC(PlayerData data) {
        super(data, "Aim", "C", "combat.aim.c", "Checks for smooth rotations (yaw).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double accelYaw = rotationProcessor.getYawAccel();

            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || rotationProcessor.getDeltaYaw() < 3.5D;

            if(accelYaw <= 0.0075 && !exempt) {
                if(++buffer > 4)
                    fail("diff=" + accelYaw);
            } else if(buffer > 0) buffer -= 0.1025;

            debug("acc=" + accelYaw + " buffer=" + buffer);
        }

    }
}
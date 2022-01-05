package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimK extends Check {

    public AimK(PlayerData data) {
        super(data, "Aim", "K", "combat.aim.k", "Checks impossible small rots (yaw).", true);

    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {

            final RotationProcessor rotationProcessor = data.getRotationProcessor();
            final double deltaYaw = rotationProcessor.getDeltaYaw();

            if(deltaYaw <= 1.0E-6 && rotationProcessor.getTicksSinceCinematic() > 3 && deltaYaw > 0) {
                if(++buffer > 1)
                    fail("delta=" + rotationProcessor.getDeltaYaw());

            } else if(buffer > 0) buffer -= 0.0025D;
        }

    }
}

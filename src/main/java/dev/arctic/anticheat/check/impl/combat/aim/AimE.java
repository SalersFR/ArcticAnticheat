package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimE extends Check {

    public AimE(PlayerData data) {
        super(data, "Aim", "E", "combat.aim.e", "Checks for rounded rots values (yaw).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final boolean isRound = deltaYaw % 0.5 == 0 || deltaYaw % 1 == 0 || deltaYaw % 1.5 == 0;

            if(deltaYaw > 0.1 && isRound) {
                if(++buffer > 4)
                    fail("delta=" + deltaYaw);
            } else if(buffer > 0) buffer -= 0.25D;
        }

    }
}

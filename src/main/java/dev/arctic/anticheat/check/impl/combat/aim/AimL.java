package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimL extends Check {

    public AimL(PlayerData data) {
        super(data, "Aim", "L", "combat.aim.l", "Checks for flaws in aim modules.", true);

    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final double deltaPitch = rotationProcessor.getDeltaPitch();

            if(deltaYaw > 7.75f && Math.abs(deltaPitch) <= 0.01 && rotationProcessor.getTicksSinceCinematic() >= 3) {
                buffer += Math.floor(deltaYaw);
                if(buffer > 35)
                    fail("delta=" + deltaYaw);

            } else if(buffer > 0) buffer -= 0.75;

            debug("delta=" + deltaYaw + " buffer=" + buffer);


        }

    }
}

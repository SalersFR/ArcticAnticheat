package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimJ extends Check {

    public AimJ(PlayerData data) {
        super(data, "Aim", "J", "combat.aim.j", "Checks for same rotation (yaw).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final double lastDeltaYaw = rotationProcessor.getLastDeltaYaw();

            final double deltaPitch = rotationProcessor.getDeltaPitch();

            if(deltaPitch > 0.4 && deltaYaw == lastDeltaYaw) {
                if(++buffer > 8) {
                    buffer /= 2.5D;
                    fail("delta=" + deltaYaw + " last=" + lastDeltaYaw);
                }
            } else if(buffer > 0) buffer--;
        }

    }
}

package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimJ1 extends Check {
    
    public AimJ1(PlayerData data) {
        super(data, "Aim", "J1", "combat.aim.j1", "Checks for same rotation (pitch).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double lastDeltaPitch = rotationProcessor.getLastDeltaPitch();

            if(deltaPitch > 0.4 && deltaPitch == lastDeltaPitch) {
                if(++buffer > 8) {
                    buffer /= 2.5D;
                    fail("delta=" + deltaPitch + " last=" + lastDeltaPitch);
                }
            } else if(buffer > 0) buffer--;
        }

    }
}

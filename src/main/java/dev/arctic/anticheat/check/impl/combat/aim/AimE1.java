package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimE1 extends Check {

    public AimE1(PlayerData data) {
        super(data, "Aim", "E1", "combat.aim.e1", "Checks for rounded rots values (pitch).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final boolean isRound = deltaPitch % 0.5 == 0 || deltaPitch % 1 == 0 || deltaPitch % 1.5 == 0;

            if(deltaPitch > 0.1 && isRound) {
                if(deltaPitch == 180.0) buffer -= 0.75D;
                if(++buffer > 1)
                    fail("delta=" + deltaPitch);
            } else if(buffer > 0) buffer -= 0.02D;
        }

    }
}

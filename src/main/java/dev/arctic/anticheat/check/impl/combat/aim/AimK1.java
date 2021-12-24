package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimK1 extends Check {

    public AimK1(PlayerData data) {
        super(data, "Aim", "K1", "combat.aim.k", "Checks impossible small rots (pitch).", true);

    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();
            final double deltaPitch = rotationProcessor.getDeltaPitch();
            if(deltaPitch <= 1.0E-8 && rotationProcessor.getTicksSinceCinematic() > 3 && deltaPitch > 0) {
                if(++buffer > 1)
                    fail("delta=" + rotationProcessor.getDeltaPitch());

            } else if(buffer > 0) buffer -= 0.0025D;
        }

    }
}
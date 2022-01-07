package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class AimA2 extends Check {




    public AimA2(PlayerData data) {
        super(data, "Aim", "A2", "combat.aim.a2", "Checks for invalid sensitivity.", true);
    }


    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();
            final int sensitivity = rotationProcessor.getSensitivity();
            debug("sens=" + sensitivity + " buffer=" + buffer);
            if(rotationProcessor.getTicksSinceCinematic() >= 5 && rotationProcessor.getDeltaPitch() != 0.0f && sensitivity == -1) {
                if(++buffer > 5)
                    fail("sens=" + sensitivity + " buffer=" + buffer);
            } else if(buffer > 0) buffer -= 0.01;

        }

    }


}

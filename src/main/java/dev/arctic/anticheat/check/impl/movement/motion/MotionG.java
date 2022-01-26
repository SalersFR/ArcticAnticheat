package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class MotionG extends Check {



    public MotionG(PlayerData data) {
        super(data, "Motion", "G", "movement.motion.g", "Checks for invalid y axis moves near ground.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {







        }
    }


}

package dev.arctic.anticheat.check.impl.player.ground;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class GroundB extends Check {

    public GroundB(PlayerData data) {
        super(data, "Ground", "B", "player.ground.b", "Checks for invalid vertical motion (GROUND)", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            final int groundTicks = data.getCollisionProcessor().getClientGroundTicks();

            final double deltaY = data.getMovementProcessor().getDeltaY();
            final double lastY = data.getMovementProcessor().getLastY();

            final boolean step = deltaY % 0.015625 == 0.0 && lastY % 0.015625 == 0.0;

            boolean exempt = data.getCollisionProcessor().isTeleporting() || data.getCollisionProcessor().isInWeb() || data.getCollisionProcessor().isInWater() || data.getCollisionProcessor().isInLava();
            final boolean invalid = groundTicks > 5 && deltaY != 0.0 && !step;

            if (invalid && !exempt) {
//                fail();
            }
        }
    }
}

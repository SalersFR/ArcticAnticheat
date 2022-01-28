package dev.arctic.anticheat.check.impl.player.ground;

import com.comphenix.packetwrapper.WrapperPlayClientFlying;
import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.Bukkit;

public class GroundA extends Check {
    public GroundA(PlayerData data) {
        super(data, "GroundSpoof", "A", "player.ground.a", "Checks if clientGround doesn't match serverGround", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isPosition()) {
            final WrapperPlayClientPosition wrapperPlayClientPosition = new WrapperPlayClientPosition(packet);

            final boolean clientGround = wrapperPlayClientPosition.getOnGround();
            final boolean serverGround = data.getMovementProcessor().getY() % 0.015625 == 0;

            final boolean exempt = data.getPlayer().isFlying() || data.getCollisionProcessor().isOnClimbable() || data.getCollisionProcessor().isTeleporting() || data.getCollisionProcessor().isInVehicle() || data.getCollisionProcessor().isLastOnSlime() || data.getCollisionProcessor().isInWater() || data.getCollisionProcessor().isInLava();
            final boolean invalid = clientGround != serverGround;

            debug(clientGround + " SG: " + serverGround);
            if (invalid && !exempt) {
                fail();
            }
        }
    }
}

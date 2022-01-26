package dev.arctic.anticheat.check.impl.player.badpackets;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

/**
 * @author xWand, 5170
 */
public class BadPacketsF extends Check {

    public BadPacketsF(PlayerData data) {
        super(data, "BadPackets", "F", "player.badpackets.f", "Checks if player is sprinting and eating.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            final boolean sprinting = data.getActionProcessor().isSprinting();
            final boolean eating = data.getActionProcessor().isEating();
            final boolean holdingEdible = data.getPlayer().getItemInHand().getType().isEdible();
            final boolean exempt = data.getCollisionProcessor().isTeleporting() || !data.getCollisionProcessor().isClientOnGround() || data.getCollisionProcessor().isInVehicle();
            final boolean invalid = eating && sprinting && holdingEdible;

            if (invalid && !exempt) {
                fail();
                data.getPlayer().setItemInHand(data.getPlayer().getItemInHand());
            }
        }
    }
}

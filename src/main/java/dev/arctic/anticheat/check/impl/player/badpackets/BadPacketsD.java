package dev.arctic.anticheat.check.impl.player.badpackets;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

/**
 * @author xWand
 */
public class BadPacketsD extends Check {

    private boolean hitEntity;

    public BadPacketsD(PlayerData data) {
        super(data, "BadPackets", "D", "player.badpackets.d", "Checks for hitting players whilst in an inventory.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isWindowClick()) {
            if (hitEntity) {
                if (++buffer > 2)
                   fail();
                else if (buffer > 0) buffer -= 0.25;
            }
        } else if (packet.isUseEntity())
            hitEntity = true;
        else if (packet.isFlying())
            hitEntity = false;

    }
}

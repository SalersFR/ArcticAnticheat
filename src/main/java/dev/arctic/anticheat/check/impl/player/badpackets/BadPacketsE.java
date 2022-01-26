package dev.arctic.anticheat.check.impl.player.badpackets;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CombatProcessor;
import dev.arctic.anticheat.packet.Packet;

/**
 * @author xWand
 */
public class BadPacketsE extends Check {

    public BadPacketsE(PlayerData data) {
        super(data, "BadPackets", "E", "player.badpackets.e", "Checks for self-hit modules.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isUseEntity()) {
            CombatProcessor processor = data.getCombatProcessor();

            if (processor.getTarget() == data.getPlayer())
                fail();
        }
    }
}

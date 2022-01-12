package dev.arctic.anticheat.check.impl.player.badpackets;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class BadPacketsG extends Check {

    public BadPacketsG(PlayerData data) {
        super(data, "BadPackets", "G", "player.badpackets.g", "Checks for an invalid confirm transaction id.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isInTransaction()) {
            if(data.getTransactionProcessor().isInvalidTransactionReply()) {
                fail();
            }
        }
    }
}

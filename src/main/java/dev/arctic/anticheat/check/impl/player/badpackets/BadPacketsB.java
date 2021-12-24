package dev.arctic.anticheat.check.impl.player.badpackets;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class BadPacketsB extends Check {

    public BadPacketsB(PlayerData data) {
        super(data, "BadPackets", "B", "player.badpackets.b", "Checks for impossible pitch.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            if(Math.abs(data.getRotationProcessor().getPitch()) > 90.0f)
                fail("invalid pitch");
        }

    }
}

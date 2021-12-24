package dev.arctic.anticheat.check.impl.player.badpackets;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class BadPacketsC extends Check {

    public BadPacketsC(PlayerData data) {
        super(data, "BadPackets", "C", "player.badpackets.c", "Checks if player is" +
                " sending action packets while sending window click packet.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            if (buffer != 0) buffer = 0;
        } else if (packet.isWindowClick()) {
            if (data.getActionProcessor().isSprinting() || data.getActionProcessor().isSneaking()) {
                if (++buffer > 1)
                    fail();
            } else if (buffer > 0) buffer /= 2;

        }
    }
}

package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.packetwrapper.WrapperPlayClientHeldItemSlot;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class ScaffoldA extends Check {

    private int lastSlot;

    public ScaffoldA(PlayerData data) {
        super(data, "Scaffold", "A", "player.scaffold.a", "Checks for invalid held item slot.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isHeldItemSlot()) {
            final WrapperPlayClientHeldItemSlot wrapped = new WrapperPlayClientHeldItemSlot(packet);

            final int slot = wrapped.getSlot();

            if (slot == lastSlot) {
                if (++buffer > 0) fail("slot=" + slot);
            } else if (buffer > 0) buffer -= 1.0E-5;

            debug("slot=" + slot + " last=" + lastSlot);


            this.lastSlot = slot;
        }

    }
}

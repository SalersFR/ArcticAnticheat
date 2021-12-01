package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.PacketReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;

public class BadPacketsF extends Check {

    private int lastItemSlot = 10;

    public BadPacketsF(PlayerData data) {
        super(data, "BadPackets", "F", "player.badpackets.f", "Checks for invalid held item slot packet.", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketReceiveEvent) {
            final PacketReceiveEvent event = (PacketReceiveEvent) e;

            if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_SLOT) {
                final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(event.getNmsPacket());

                if (wrapper.getCurrentSelectedSlot() == lastItemSlot) {
                    if (++buffer > 0)
                        fail("current=" + wrapper.getCurrentSelectedSlot() + " last=" + lastItemSlot);
                } else if (buffer > 0) buffer -= 0.001D;

                this.lastItemSlot = wrapper.getCurrentSelectedSlot();
            }
        }
    }
}

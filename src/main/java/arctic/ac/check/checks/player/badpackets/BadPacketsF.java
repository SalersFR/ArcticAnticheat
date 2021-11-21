package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.packetwrapper.WrapperPlayClientHeldItemSlot;
import com.comphenix.protocol.PacketType;

public class BadPacketsF extends Check {

    private int lastItemSlot = 10;

    public BadPacketsF(PlayerData data) {
        super(data, "BadPackets", "F", "player.badpackets.f", "Checks for invalid held item slot packet.", false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof PacketEvent) {
            final PacketEvent event = (PacketEvent) e;

            if(event.getPacketType() == PacketType.Play.Client.HELD_ITEM_SLOT) {
                final WrapperPlayClientHeldItemSlot wrapper = new WrapperPlayClientHeldItemSlot(event.getContainer());

                if(wrapper.getSlot() == lastItemSlot) {
                    if(++buffer > 0)
                        fail("current=" + wrapper.getSlot() + " last=" + lastItemSlot);
                } else if(buffer > 0) buffer -= 0.001D;

                this.lastItemSlot = wrapper.getSlot();
            }
        }
    }
}

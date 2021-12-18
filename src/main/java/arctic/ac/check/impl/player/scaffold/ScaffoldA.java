package arctic.ac.check.impl.player.scaffold;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInHeldItemSlot;

public class ScaffoldA extends Check {

    private int lastSlot;

    public ScaffoldA(PlayerData data) {
        super(data, "Scaffold", "A", "player.scaffold.a","Checks for invalid held item slot.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_HELD_ITEM_SLOT) {
            final WrappedInHeldItemSlot wrapped = new WrappedInHeldItemSlot(packet);
            final int slot = wrapped.getSlot();

            if(slot == lastSlot) {
                if(++buffer > 0) fail("slot=" + slot);
            } else if(buffer > 0) buffer -= 1.0E-9;

            debug("slot=" + slot + " last=" + lastSlot);



            this.lastSlot = slot;
        }
    }


}

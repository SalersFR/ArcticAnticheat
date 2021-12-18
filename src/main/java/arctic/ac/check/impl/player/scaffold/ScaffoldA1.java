package arctic.ac.check.impl.player.scaffold;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInHeldItemSlot;

public class ScaffoldA1 extends Check {

    private int lastSlot;

    public ScaffoldA1(PlayerData data) {
        super(data, "Scaffold", "A1", "player.scaffold.a","Checks for invalid held item slot.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_HELD_ITEM_SLOT) {
            final WrappedInHeldItemSlot wrapped = new WrappedInHeldItemSlot(packet);
            final int slot = wrapped.getSlot();

            if(slot != lastSlot && data.getClickProcessor().getPlaceTicks() <= 0 && data.getRotationProcessor().getDeltaYaw()
                    > 1.5F && data.getRotationProcessor().getDeltaPitch() != .0F && Math.abs(slot - lastSlot) <= 2) {
                if(++buffer > 4) fail("slot=" + slot);
            } else if(buffer > 0) buffer -= 0.2;

            debug("slot=" + slot + " last=" + lastSlot + " ticks=" + data.getClickProcessor().getPlaceTicks());



            this.lastSlot = slot;
        }
    }
}

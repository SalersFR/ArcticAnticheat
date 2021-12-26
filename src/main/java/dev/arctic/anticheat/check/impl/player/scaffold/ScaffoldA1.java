package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.packetwrapper.WrapperPlayClientHeldItemSlot;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class ScaffoldA1 extends Check {

    private int lastSlot;

    public ScaffoldA1(PlayerData data) {
        super(data, "Scaffold", "A1", "player.scaffold.a","Checks for invalid held item slot.", false);
    }


    @Override
    public void handle(Packet packet, long time) {
        if(packet.isHeldItemSlot()) {
            final WrapperPlayClientHeldItemSlot wrapped = new WrapperPlayClientHeldItemSlot(packet);

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

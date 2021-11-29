package arctic.ac.event.client;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.utils.player.Hand;
import lombok.Getter;

@Getter
public class BlockPlaceEvent extends Event {

    private final Hand hand;
    private final long time;

    public BlockPlaceEvent(final WrappedPacketInBlockPlace wrapper) {
        this.hand = wrapper.getHand();
        this.time = System.currentTimeMillis();
    }
}

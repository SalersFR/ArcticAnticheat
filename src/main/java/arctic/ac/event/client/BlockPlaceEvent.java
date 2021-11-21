package arctic.ac.event.client;

import arctic.ac.event.Event;
import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;

@Getter
public class BlockPlaceEvent extends Event {

    private final EnumWrappers.Hand hand;
    private final long time;

    public BlockPlaceEvent(final WrapperPlayClientBlockPlace wrapper) {
        this.hand = wrapper.getHand();
        this.time = wrapper.getTimestamp();
    }
}

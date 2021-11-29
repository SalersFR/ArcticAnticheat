package arctic.ac.event.client;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import lombok.Getter;

@Getter
public class EntityActionEvent extends Event {

    private WrappedPacketInEntityAction.PlayerAction action;

    public EntityActionEvent(WrappedPacketInEntityAction wrapper) {
        this.action = wrapper.getAction();
    }

}

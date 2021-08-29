package arctic.ac.event.client;

import arctic.ac.event.Event;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;

@Getter
public class EntityActionEvent extends Event {

    private EnumWrappers.PlayerAction action;

    public EntityActionEvent(WrapperPlayClientEntityAction wrapper) {
        this.action = wrapper.getAction();
    }

}

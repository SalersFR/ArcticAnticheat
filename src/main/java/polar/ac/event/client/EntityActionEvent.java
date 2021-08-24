package polar.ac.event.client;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import polar.ac.event.Event;

@Getter
public class EntityActionEvent extends Event {

    private EnumWrappers.PlayerAction action;
    public EntityActionEvent(WrapperPlayClientEntityAction wrapper) {
        this.action = wrapper.getAction();
    }
}
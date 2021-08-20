package honeybadger.ac.event.client;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.event.Event;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;

@Getter
public class UseEntityEvent extends Event {

    private final EnumWrappers.EntityUseAction action;
    private final WrapperPlayClientUseEntity wrapper;
    private final Entity target;

    public UseEntityEvent(WrapperPlayClientUseEntity wrapper, World world) {

        this.wrapper = wrapper;
        this.action = wrapper.getType();
        this.target = wrapper.getTarget(world);
    }
}

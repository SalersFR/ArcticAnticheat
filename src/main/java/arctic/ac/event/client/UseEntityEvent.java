package arctic.ac.event.client;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;

@Getter
public class UseEntityEvent extends Event {

    private final WrappedPacketInUseEntity.EntityUseAction action;
    private final WrappedPacketInUseEntity wrapper;
    private final Entity target;

    public UseEntityEvent(WrappedPacketInUseEntity wrapper, World world) {

        this.wrapper = wrapper;
        this.action = wrapper.getAction();
        this.target = wrapper.getEntity();
    }
}

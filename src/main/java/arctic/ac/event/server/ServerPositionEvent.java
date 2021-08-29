package arctic.ac.event.server;

import arctic.ac.event.Event;
import com.comphenix.packetwrapper.WrapperPlayServerPosition;
import lombok.Getter;

import java.util.Set;

@Getter
public class ServerPositionEvent extends Event {

    private double yaw, pitch, x, y, z;
    private Set<WrapperPlayServerPosition.PlayerTeleportFlag> flags;

    public ServerPositionEvent(WrapperPlayServerPosition wrapper) {

        this.x = wrapper.getX();
        this.y = wrapper.getY();
        this.z = wrapper.getZ();

        this.pitch = wrapper.getPitch();
        this.yaw = wrapper.getYaw();

        this.flags = wrapper.getFlags();
    }
}

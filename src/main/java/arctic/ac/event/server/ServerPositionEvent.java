package arctic.ac.event.server;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import lombok.Getter;

@Getter
public class ServerPositionEvent extends Event {

    private double yaw, pitch, x, y, z;


    public ServerPositionEvent(WrappedPacketOutPosition wrapper) {


        this.x = wrapper.getPosition().getX();
        this.y = wrapper.getPosition().getY();
        this.z = wrapper.getPosition().getZ();

        this.pitch = wrapper.getPitch();
        this.yaw = wrapper.getYaw();

    }
}

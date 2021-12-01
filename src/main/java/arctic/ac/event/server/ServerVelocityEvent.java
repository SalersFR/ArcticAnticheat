package arctic.ac.event.server;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import lombok.Getter;


public class ServerVelocityEvent extends Event {

    @Getter
    private final WrappedPacketOutEntityVelocity wrapper;
    private final double x, y, z;
    private final int entityID;

    public ServerVelocityEvent(WrappedPacketOutEntityVelocity wrapper) {
        this.wrapper = wrapper;

        this.x = wrapper.getVelocityX();
        this.y = wrapper.getVelocityY();
        this.z = wrapper.getVelocityZ();

        this.entityID = wrapper.getEntityId();


    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getEntityID() {
        return entityID;
    }
}

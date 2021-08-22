package polar.ac.event.server;

import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import polar.ac.event.Event;

public class ServerVelocityEvent extends Event {

    private final WrapperPlayServerEntityVelocity wrapper;
    private final double x, y, z;
    private final int entityID;

    public ServerVelocityEvent(WrapperPlayServerEntityVelocity wrapper) {
        this.wrapper = wrapper;

        this.x = wrapper.getVelocityX();
        this.y = wrapper.getVelocityY();
        this.z = wrapper.getVelocityZ();

        this.entityID = wrapper.getEntityID();


    }

    public WrapperPlayServerEntityVelocity getWrapper() {
        return wrapper;
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

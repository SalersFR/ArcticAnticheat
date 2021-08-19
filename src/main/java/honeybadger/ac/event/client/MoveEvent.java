package honeybadger.ac.event.client;

import com.comphenix.protocol.PacketType;

import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.utils.HLocation;

public class MoveEvent extends Event {

    private HLocation to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;

    public MoveEvent(PlayerData data, double x, double y, double z) {

        this.from = data.getLocation();
        this.to = new HLocation(x, y, z);
        data.setLocation(this.to);

        if(to == null || from == null) return;



        this.deltaY = to.getY() - from.getY();

        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();

        this.deltaXZ = (deltaX * deltaX) + (deltaZ * deltaZ);

    }

    public HLocation getTo() {
        return to;
    }

    public HLocation getFrom() {
        return from;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }
   

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getDeltaXZ() {
        return deltaXZ;
    }

    public void setFrom(HLocation from) {
        this.from = from;
    }
}

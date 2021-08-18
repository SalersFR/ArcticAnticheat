package honeybadger.ac.event.client;
import honeybadger.ac.event.Event;
import honeybadger.ac.utils.HLocation;
import org.bukkit.Location;

public class MoveEvent extends Event {

    private HLocation to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;

    public MoveEvent(double x,double y,double z) {

        this.from = this.to == null ? null : this.to.clone();
        this.to = new HLocation(x, y, z);

        if(to == null || from == null) return;



        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();

        this.deltaXZ = (deltaX * deltaX) + (deltaZ * deltaZ);

        this.deltaY = to.getY() - from.getY();

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

package polar.ac.event.client;

import lombok.Getter;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.utils.PLocation;

@Getter
public class MoveEvent extends Event {

    private PLocation to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;
    private boolean ground;

    public MoveEvent(PlayerData data, double x, double y, double z, boolean ground) {
        this.from = data.getLocation();
        this.to = new PLocation(x, y, z);
        data.setLocation(this.to);

        if (to == null || from == null) return;

        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();

        this.deltaXZ = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        this.deltaY = to.getY() - from.getY();

        this.ground = ground;
    }
}

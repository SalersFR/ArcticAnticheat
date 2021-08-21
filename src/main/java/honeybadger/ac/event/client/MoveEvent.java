package honeybadger.ac.event.client;

import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.utils.HLocation;
import lombok.Getter;

@Getter
public class MoveEvent extends Event {

    private HLocation to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;
    private boolean ground;

    public MoveEvent(PlayerData data, double x, double y, double z, boolean ground) {
        this.from = data.getLocation();
        this.to = new HLocation(x, y, z);
        data.setLocation(this.to);

        if (to == null || from == null) return;

        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();

        this.deltaXZ = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        this.deltaY = to.getY() - from.getY();

        this.ground = ground;
    }
}

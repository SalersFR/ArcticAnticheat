package arctic.ac.event.client;

import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.utils.ALocation;
import lombok.Getter;

@Getter
public class MoveEvent extends Event {

    private ALocation to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;
    private boolean ground;

    public MoveEvent(PlayerData data, double x, double y, double z,float yaw,float pitch, boolean ground) {
        this.from = data.getLocation();
        this.to = new ALocation(x, y, z,yaw,pitch);
        data.setLocation(this.to);

        if (to == null || from == null) return;

        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();

        this.deltaXZ = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        this.deltaY = to.getY() - from.getY();

        this.ground = ground;
    }
}

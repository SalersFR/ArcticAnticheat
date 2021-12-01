package arctic.ac.event.client;

import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.utils.APosition;
import lombok.Getter;

@Getter
public class PositionEvent extends Event {

    private APosition to, from;
    private double deltaX, deltaY, deltaZ, deltaXZ;
    private boolean ground;
    private float pitch;
    private float yaw;

    public PositionEvent(PlayerData data, double x, double y, double z, float yaw, float pitch, boolean ground) {
        this.from = data.getPosition();
        this.to = new APosition(x, y, z, yaw, pitch);
        data.setPosition(this.to);

        if (to == null || from == null) return;

        this.deltaX = to.getX() - from.getX();
        this.deltaZ = to.getZ() - from.getZ();
        this.yaw = yaw;
        this.pitch = pitch;

        this.deltaXZ = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        this.deltaY = to.getY() - from.getY();

        this.ground = ground;
    }
}

package arctic.ac.event.client;

import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.utils.ARotation;
import lombok.Getter;

@Getter
public class RotationEvent extends Event {

    private ARotation from, to;
    private float deltaYaw, deltaPitch;

    public RotationEvent(PlayerData data, float yaw, float pitch) {
        this.from = data.getRotation();
        this.to = new ARotation(yaw % 360, pitch);
        data.setRotation(this.to);

        if (to == null || from == null) return;

        this.deltaYaw = Math.abs(from.getYaw() % 360 - to.getYaw() % 360) % 360;
        this.deltaPitch = Math.abs(from.getPitch() - to.getPitch());
    }
}

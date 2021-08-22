package polar.ac.event.client;

import lombok.Getter;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.utils.PRotation;

@Getter
public class RotationEvent extends Event {

    private PRotation from, to;
    private float deltaYaw, deltaPitch;

    public RotationEvent(PlayerData data, float yaw, float pitch) {
        this.from = data.getRotation();
        this.to = new PRotation(yaw % 360, pitch);
        data.setRotation(this.to);

        if (to == null || from == null) return;

        this.deltaYaw = Math.abs(from.getYaw() % 360 - to.getYaw() % 360) % 360;
        this.deltaPitch = Math.abs(from.getPitch() - to.getPitch());
    }
}

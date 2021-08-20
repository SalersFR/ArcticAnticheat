package honeybadger.ac.event.client;

import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.utils.HRotation;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class RotationEvent extends Event {

    private HRotation from, to;
    private float deltaYaw, deltaPitch;

    public RotationEvent(PlayerData data, float yaw, float pitch) {
        this.from = data.getRotation();
        this.to = new HRotation(yaw % 360, pitch);
        data.setRotation(this.to);

        this.deltaYaw = Math.abs(from.getYaw() % 360 - to.getYaw() % 360) % 360;
        this.deltaPitch = Math.abs(from.getPitch() - to.getPitch());
    }
}

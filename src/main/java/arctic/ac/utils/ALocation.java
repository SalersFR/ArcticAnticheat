package arctic.ac.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@Getter
@Setter
public class ALocation {

    private double x, y, z;

    private float yaw, pitch;

    public ALocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ALocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ALocation clone() {
        return new ALocation(x, y, z);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }


    public ALocation subtract(ALocation vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }


}

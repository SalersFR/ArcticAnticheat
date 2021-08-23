package polar.ac.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PLocation {

    private double x, y, z;

    private float yaw, pitch;

    public PLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PLocation clone() {
        return new PLocation(x, y, z);
    }
}
package honeybadger.ac.utils;

import lombok.Getter;

@Getter
public class HLocation {

    private double x, y, z;

    private float yaw, pitch;

    public HLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public HLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public HLocation clone() {
        return new HLocation(x, y, z);
    }
}

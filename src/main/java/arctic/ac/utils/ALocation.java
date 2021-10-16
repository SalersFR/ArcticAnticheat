package arctic.ac.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
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

    public boolean isMathOnGround() {
        return this.y % 0.015625 == 0.0;
    }

    public boolean isCollOnGround(final World world) {
        return new WorldUtils().isCloseToGround(new Location(world,x,y,z));
    }


}

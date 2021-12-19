package arctic.ac.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@Getter
@Setter
@AllArgsConstructor
public class APosition {

    private double x, y, z;
    private float yaw, pitch;

    public Vector toVector() {
        return new Vector(x, y, z);
    }



}

package arctic.ac.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.EntityType;

@Data
@AllArgsConstructor
public class AEntity {

    private double x, y, z;
    private EntityType type;
    private int id;

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public void addZ(double z) {
        this.z += z;
    }
}

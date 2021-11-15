package arctic.ac.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class EntityId {
    @Getter @Setter
    private double x, y, z, newX, newY, newZ, relX, relY, relZ;
    @Getter @Setter
    private EntityType type;
    @Getter @Setter
    private int id, interpolationSteps;
    @Getter @Setter
    private short transactionID = -900;
    @Getter @Setter
    private HashMap<Short, Vector> transactionTimes = new HashMap<>();

    public EntityId(int id,double x,double y,double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityId(int id) {
        this.id = id;
    }
}

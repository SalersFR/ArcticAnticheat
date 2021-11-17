package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ReachEntity {

    @Getter @Setter
    private double x, y, z, newX, newY, newZ, relX, relY, relZ;
    @Getter @Setter
    private EntityType type;
    @Getter @Setter
    private int id, interpolationSteps = 0;
    @Getter @Setter
    private short transactionID = -900;
    @Getter
    @Setter
    private HashMap<Short, Vector> transactionTimes = new HashMap<>();

    public ReachEntity(int id,double x,double y,double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ReachEntity(int id) {
        this.id = id;
    }
}

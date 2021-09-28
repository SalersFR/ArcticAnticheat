package arctic.ac.data.tracker;

import arctic.ac.utils.ConcurrentList;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;

@Getter
public class TrackedEntity {

    private EntityLocation entityLocation;
    private int id;

    public void onUpdate(EntityUpdate update) {
        if (update.isConfirmed()) {

        }
    }

    public TrackedEntity(int id) {
        this.id = id;
    }

    public Entity getBukkitEntity(final World world) {
        for(Entity entities : world.getEntities()) {
            if(entities.getEntityId() == id) {
                return entities;
            }
        }
        return null;
    }
}

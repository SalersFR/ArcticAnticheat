package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import com.comphenix.packetwrapper.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TargetTracker {

    private final PlayerData data;
    private final List<TrackedEntity> trackedEntityList = new ArrayList<>();

    public void handleRelMove(final WrapperPlayServerRelEntityMove packet) {
        for (TrackedEntity tracked : trackedEntityList) {
            if (tracked.getId() == packet.getEntityID()) {

                final double x = packet.getDx() / 32D;
                final double y = packet.getDy() / 32D;
                final double z = packet.getDz() / 32D;

                tracked.getEntityLocation().move(x, y, z);
            }
        }


    }

    public void handleRelMoveLook(final WrapperPlayServerRelEntityMoveLook packet) {
        for (TrackedEntity tracked : trackedEntityList) {
            if (tracked.getId() == packet.getEntityID()) {

                final double x = packet.getDx() / 32D;
                final double y = packet.getDy() / 32D;
                final double z = packet.getDz() / 32D;

                tracked.getEntityLocation().move(x, y, z);
            }
        }


    }

    public void handleTeleport(final WrapperPlayServerEntityTeleport packet, final World world) {
        for (TrackedEntity trackedEntity : this.trackedEntityList) {
            if (trackedEntity.getId() == packet.getEntityID()) {


                double d0 = (double) packet.getX() / 32D;
                double d1 = (double) packet.getY() / 32D;
                double d2 = (double) packet.getZ() / 32D;

                Entity entity = trackedEntity.getBukkitEntity(world);

                Location location = entity.getLocation();

                double posX = location.getX();
                double posY = location.getY();
                double posZ = location.getZ();

                if (Math.abs(posX - d0) < 0.03125D && Math.abs(posY - d1) < 0.015625D && Math.abs(posZ - d2) < 0.03125D) {
                    trackedEntity.getEntityLocation().teleport(posX, posY, posZ);
                } else {
                    trackedEntity.getEntityLocation().teleport(d0, d1, d2);
                }


            }
        }

    }

    public void handleNamedSpawn(final WrapperPlayServerNamedEntitySpawn packet) {
        final double x = packet.getX() / 32D;
        final double y = packet.getY() / 32D;
        final double z = packet.getZ() / 32D;

        add(x, y, z, packet.getEntityID());

    }

    public void handleEntitySpawn(final WrapperPlayServerSpawnEntity packet) {
        final double x = packet.getX() / 32D;
        final double y = packet.getY() / 32D;
        final double z = packet.getZ() / 32D;

        add(x, y, z, packet.getEntityID());
    }

    public void handleLivingSpawn(final WrapperPlayServerSpawnEntityLiving packet) {
        final double x = packet.getX() / 32D;
        final double y = packet.getY() / 32D;
        final double z = packet.getZ() / 32D;

        add(x, y, z, packet.getEntityID());
    }

    private void add(double x, double y, double z, int id) {
        final TrackedEntity toAdd = new TrackedEntity(id);
        trackedEntityList.add(toAdd);
    }


}

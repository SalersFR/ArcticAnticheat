package dev.arctic.anticheat.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class ServerUtil {

    public Block getBlock(Location location) {
        if (location.getChunk().isLoaded()) {
            return location.getBlock();
        }

        return null;
    }

    public List<Entity> getEntitiesWithinRadius(Player player, Location location, double radius) {

        double expander = 16.0D;

        double x = location.getX();
        double z = location.getZ();

        int minX = (int) Math.floor((x - radius) / expander);
        int maxX = (int) Math.floor((x + radius) / expander);

        int minZ = (int) Math.floor((z - radius) / expander);
        int maxZ = (int) Math.floor((z + radius) / expander);

        World world = location.getWorld();

        List<Entity> entities = new LinkedList<>();

        for (int xVal = minX; xVal <= maxX; xVal++) {

            for (int zVal = minZ; zVal <= maxZ; zVal++) {

                if (!world.isChunkLoaded(xVal, zVal)) continue;

                for (Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {
                    //We have to do this due to stupidness
                    if (entity == null && entity.getEntityId() != player.getEntityId()) continue;

                    //Make sure the entity is within the radius specified
                    if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                    entities.add(entity);
                }
            }
        }

        return entities;
    }

}

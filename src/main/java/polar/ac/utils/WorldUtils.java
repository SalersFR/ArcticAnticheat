package polar.ac.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldUtils {

    /**
     * @param loc the location of the block to get
     * @return the block at the location
     */
    private Block getBlockAsync(final Location loc) {
        if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4))
            return loc.getBlock();
        return null;
    }

    public Location getBehindPlayer(Player player) {
        Location finalLoc = player.getLocation();
        String directionCard = getCardinalDirection(player);
        switch (directionCard) {
            case "N":
                finalLoc.add(0, 0, 3);
                break;
            case "S":
                finalLoc.add(0, 0, -3);
                break;
            case "W":
                finalLoc.add(6, 0, 0);
                break;
            case "E":
                finalLoc.add(-3, 0, 0);
                break;
        }

        return finalLoc;
    }

    /**
     * Checking if a location is close to ground
     *
     * @param location the location to check
     * @return if the location is close to ground
     */

    public boolean isCloseToGround(Location location) {
        double distanceToGround = 0.3;
        for (double locX = -distanceToGround; locX <= distanceToGround; locX += distanceToGround) {
            for (double locZ = -distanceToGround; locZ <= distanceToGround; locZ += distanceToGround) {
                if (location.clone().add(0, -0.5001, 0).getBlock().getType() == Material.AIR) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Check if player is in liquid
     *
     * @param player the player to check
     * @return if player is in liquid
     */
    public boolean isInLiquid(final Player player) {
        final double expand = 0.31;
        final Location location = player.getLocation();
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (getBlockAsync(location.clone().add(x, -0.5001, z)).isLiquid() || location.getBlock().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if player is near a boat
     *
     * @param player the player to check
     * @return if player is near a boat
     */
    public boolean isNearBoat(final Player player) {
        for (final Entity entity : player.getNearbyEntities(3, 3, 3)) {
            if (entity instanceof Boat) return true;
        }
        return false;
    }

    /**
     * Checking if player is colliding with a climbable
     *
     * @param player the player to check
     * @return if player is colling with a climbable
     */

    public boolean isCollidingWithClimbable(final Player player) {
        final Location location = player.getLocation();
        final int var1 = MathUtils.floor(location.getX());
        final int var2 = MathUtils.floor(location.getY());
        final int var3 = MathUtils.floor(location.getZ());
        final Block var4 = new Location(location.getWorld(), var1, var2, var3).getBlock();
        return var4.getType() == Material.LADDER || var4.getType() == Material.VINE;
    }

    /**
     * Checking if player is colliding with web
     *
     * @param player the player to check
     * @return if player is colling with web
     */

    public boolean isCollidingWithWeb(final Player player) {
        final Location location = player.getLocation();
        final int var1 = MathUtils.floor(location.getX());
        final int var2 = MathUtils.floor(location.getY());
        final int var3 = MathUtils.floor(location.getZ());
        final Block var4 = new Location(location.getWorld(), var1, var2, var3).getBlock();
        return var4.getType() == Material.WEB;
    }

    /**
     * Checking if player is at the edge of a block
     *
     * @param player the player to check
     * @return if player is at the edge of a block
     */
    public boolean isAtEdgeOfABlock(final Player player) {
        Location b1 = player.getLocation().clone().add(0.3, -0.3, -0.3);
        Location b2 = player.getLocation().clone().add(-0.3, -0.3, -0.3);
        Location b3 = player.getLocation().clone().add(0.3, -0.3, 0.3);
        Location b4 = player.getLocation().clone().add(-0.3, -0.3, +0.3);
        return b1.getBlock().getType() != Material.AIR || b2.getBlock().getType() != Material.AIR ||
                b3.getBlock().getType() != Material.AIR || b4.getBlock().getType() != Material.AIR;

    }

    public boolean isOnACertainBlock(final Player player, String contains) {
        final Location location = player.getLocation();
        double distanceToGround = 0.3;
        for (double locX = -distanceToGround; locX <= distanceToGround; locX += distanceToGround) {
            for (double locZ = -distanceToGround; locZ <= distanceToGround; locZ += distanceToGround) {
                if (location.clone().add(0, -0.5001, 0).getBlock().toString().toLowerCase().contains(contains)) {
                    return true;
                }
            }

        }
        return false;
    }




    public String getCardinalDirection(Entity e) {

        double rotation = (e.getLocation().getYaw() - 90.0F) % 360.0F;

        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D))
            return "W";
        if ((45.0D <= rotation) && (rotation < 135.0D))
            return "N";
        if ((135.0D <= rotation) && (rotation < 225.0D))
            return "E";
        if ((225.0D <= rotation) && (rotation < 315.0D))
            return "S";
        if ((315.0D <= rotation) && (rotation < 360.0D)) {
            return "W";
        }
        return null;
    }
}

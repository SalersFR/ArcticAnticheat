package polar.ac.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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

    public EntityPlayer spawnFakePlayer(Player player, String displayname) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

        Player target = Bukkit.getServer().getPlayer(displayname);
        EntityPlayer npc;
        if (target != null) {
            npc = new EntityPlayer(server, world, new GameProfile(target.getUniqueId(), target.getName()), new PlayerInteractManager(world));
        } else {
            OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(displayname);
            npc = new EntityPlayer(server, world, new GameProfile(op.getUniqueId(), displayname), new PlayerInteractManager(world));
        }
        Location loc = player.getLocation();
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        for (Player all : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        }

        return npc;
    }

    public Location getBehindPlayer(Player player) {
        Vector dirOpp = player.getLocation().getDirection().normalize().multiply(-1);
        return player.getLocation().add(dirOpp);
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
}

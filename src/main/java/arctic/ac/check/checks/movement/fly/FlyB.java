package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FlyB extends Check {

    public int clientAirTicks;
    private int airTicks;

    public FlyB(PlayerData data) {
        super(data, "Fly", "B", "movement.fly.b", "Checks if player is not taking care about gravity.", true);
    }

    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;


            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();

            final double deltaY = event.getDeltaY();
            final double motionY = player.getVelocity().getY();

            final double resultY = deltaY - motionY;
            final boolean nearGround = isNearBlocks(event.getTo().toVector().toLocation(player.getWorld()));

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || airTicks < 11
                    || player.getFallDistance() > 10.0F
                    || event.isGround()
                    || nearGround
                    || player.isInsideVehicle();

            final boolean exemptV2 = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || clientAirTicks < 11
                    || player.getFallDistance() > 10.0F
                    || event.isGround()
                    || player.isInsideVehicle();


            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            if (!event.isGround()) {
                clientAirTicks++;
            } else clientAirTicks = 0;

            debug("deltaY=" + deltaY + " motionY=" + motionY + " resultY=" + resultY + " exempt=" + exempt);

            if (resultY > 1.0D && !exempt) {
                if (++buffer > 1) {
                    fail("resultY=" + resultY + " way1");
                }
            } else if (buffer > 0) buffer -= 0.05D;

            if (resultY > 1.0D && !exemptV2) {
                if (++buffer > 1) {
                    fail("resultY=" + resultY + " way2");
                }
            } else if (buffer > 0) buffer -= 0.05D;
        }
    }

    public boolean isNearBlocks(Location location) {
        Location min = location.toVector().add(new Vector(0.8, -5, 0.8)).toLocation(location.getWorld());
        Location max = location.toVector().subtract(new Vector(0.8, 0, 0.8)).toLocation(location.getWorld());
        for (Block block : blocksFromTwoPoints(min, max)) {
            if (block.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }
}
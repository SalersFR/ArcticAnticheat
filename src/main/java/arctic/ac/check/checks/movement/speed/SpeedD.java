package arctic.ac.check.checks.movement.speed;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.PositionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SpeedD extends Check {

    private Vector lastMotion;
    private boolean lastOnGround;
    private boolean lastLastOnGround;
    private Vector moveDirection;
    private float yaw;
    public SpeedD(PlayerData data) {
        super(data, "Speed", "D", "movement.speed.d", "Checks if player is following minecraft's strafe movement protocol in ground.", true);
    }

    public static double angle(Vector a, Vector b) {
        double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
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

        if (e instanceof PositionEvent) {
            PositionEvent move = (PositionEvent) e;

            Vector motion = move.getTo().toVector().setY(0).subtract(move.getFrom().toVector().setY(0));
            Vector lastMotion = this.lastMotion;
            this.lastMotion = motion;

            final boolean isOnGround = move.isGround();
            final boolean lastOnGround = this.lastOnGround;
            final boolean lastLastOnGround = this.lastLastOnGround;
            this.lastOnGround = isOnGround;
            this.lastLastOnGround = lastOnGround;

            float friction = 0.91F;
            if (lastLastOnGround) {
                friction = (float) 0.6;
            }

            lastMotion.multiply(friction);

            boolean attackSlowDown = data.getInteractData().isHasHitSlowDown();
            if (Math.abs(lastMotion.getX()) < 0.003) lastMotion = new Vector(0, 0, lastMotion.getZ());
            if (Math.abs(lastMotion.getZ()) < 0.003) lastMotion = new Vector(lastMotion.getX(), 0, 0);

            boolean inBlocks = isNearBlocks(move.getTo().toVector().toLocation(data.getPlayer().getWorld()));

            final float yaw = move.getYaw();
            final double yawRadians = Math.toRadians(yaw);

            if (attackSlowDown) {
                lastMotion.multiply(0.6);
            }

            final Vector look = new Vector(-Math.sin(yawRadians), 0.0, Math.cos(yawRadians));
            final Vector moveDir = motion.subtract(lastMotion);
            ;

            double angle = angle(look, moveDir);
            angle = angle % (Math.PI / 4);

            if (angle > 0.5 && angle < (Math.PI / 4 - 0.05) && move.getDeltaXZ() > 0.045) {
                if(++buffer > 7)
                    fail("angle=" + angle);
            } else if(buffer > 0) buffer -= 0.3333343D;

            //debug("angle=" + angle + " buffer" + buffer);
        }


    }

    public boolean isNearBlocks(Location location) {
        Location min = location.toVector().add(new Vector(0.6, 1, 0.6)).toLocation(location.getWorld());
        Location max = location.toVector().subtract(new Vector(0.6, 0, 0.6)).toLocation(location.getWorld());
        for (Block block : blocksFromTwoPoints(min, max)) {
            if (block.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }
}
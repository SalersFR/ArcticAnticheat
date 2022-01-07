package dev.arctic.anticheat.check.impl.movement.speed;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SpeedB extends Check {
    private Vector lastMotion;
    private boolean lastOnGround;
    private boolean lastLastOnGround;

    public SpeedB(PlayerData data) {
        super(data, "Speed", "B", "movement.speed.b", "Checks if player is following minecraft's strafe movement protocol in ground.", true);
    }
    public static double angle(Vector a, Vector b) {
        double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
    }
    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();
        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));
        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));
        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
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
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            Vector motion = movementProcessor.getLocation().toBukkitVec().setY(0).subtract(new Vector(movementProcessor.getLastX(),0,movementProcessor.getLastZ()));
            Vector lastMotion = this.lastMotion;
            this.lastMotion = motion;
            final boolean isOnGround = collisionProcessor.isClientOnGround();
            final boolean lastOnGround = this.lastOnGround;
            final boolean lastLastOnGround = this.lastLastOnGround;
            this.lastOnGround = isOnGround;
            this.lastLastOnGround = lastOnGround;
            float friction = 0.91F;
            if (lastLastOnGround) {
                friction = (float) 0.6;
            }
            lastMotion.multiply(friction);
            boolean attackSlowDown = data.getMovementProcessor().isSlowDown();
            if (Math.abs(lastMotion.getX()) < 0.003) lastMotion = new Vector(0, 0, lastMotion.getZ());
            if (Math.abs(lastMotion.getZ()) < 0.003) lastMotion = new Vector(lastMotion.getX(), 0, 0);
            boolean inBlocks = isNearBlocks(movementProcessor.getLocation().toBukkitVec().toLocation(data.getPlayer().getWorld()));
            final float yaw = rotationProcessor.getYaw();
            final double yawRadians = Math.toRadians(yaw);
            if (attackSlowDown) {
                lastMotion.multiply(0.6);
            }
            final Vector look = new Vector(-Math.sin(yawRadians), 0.0, Math.cos(yawRadians));
            final Vector moveDir = motion.subtract(lastMotion);
            ;
            double angle = angle(look, moveDir);
            angle = angle % (Math.PI / 4);

            if (angle > 0.05 && angle < (Math.PI / 4 - 0.05) && movementProcessor.getDeltaXZ() > 0.045) {
                Bukkit.broadcastMessage("angle " + angle);
            }
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

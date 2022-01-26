package dev.arctic.anticheat.check.impl.movement.speed;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;
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
    private double lastMotionX;
    private double lastMotionZ;

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

            //Other check
            double motionX = movementProcessor.getDeltaX();
            double motionZ = movementProcessor.getDeltaZ();

            double lastMotionX = this.lastMotionX;
            double lastMotionZ = this.lastMotionZ;

            this.lastMotionX = motionX;
            this.lastMotionZ = motionZ;

            //friction
            float friction = 0.91F;
            if (lastLastOnGround) {
                friction = (float) getFriction(new Location(data.getPlayer().getWorld(), movementProcessor.getLastX(),movementProcessor.getLastY(),movementProcessor.getLastZ()),collisionProcessor.isLastClientOnGround());
            }

            lastMotionX = lastMotionX * friction;
            lastMotionZ = lastMotionZ * friction;

            boolean attackSlowDown = data.getMovementProcessor().isSlowDown();
            if (attackSlowDown) {
                lastMotionX *= 0.6;
                lastMotionZ *= 0.6;
            }

            if (Math.abs(lastMotionZ) < 0.003) lastMotionZ = 0.0;
            if (Math.abs(lastMotionX) < 0.003) lastMotionX = 0.0;

            final float yaw = rotationProcessor.getYaw();
            final double yawRadians = Math.toRadians(yaw);

            final Vector direction = new Vector(-Math.sin(yawRadians), 0.0, Math.cos(yawRadians));
            final Vector movementDirection = new Vector(motionX - lastMotionX, 0.0, motionZ - lastMotionZ);

            double angle = angle(direction, movementDirection);
            angle = angle % (Math.PI / 4);

            if (collisionProcessor.isClientOnGround() && angle > 0.05 && angle < (Math.PI / 4 - 0.05)) {
                //TODO pathYaw.
                //data.getPlayer().sendMessage("angle " + angle + " yaw " + yaw);
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

    @SuppressWarnings("deprecation")
    public static double getFriction(Location loc1,boolean onGround) {
        double Friction = 0.91;
        String getBlock = "stone";
        Location location = loc1.toVector().subtract(new Vector(0,1,0)).toLocation(loc1.getWorld());

        if (!onGround) getBlock = "air";
        if (location.getBlock().getType() == Material.SLIME_BLOCK) getBlock = "slime";
        if (location.getBlock().getType() == Material.ICE) getBlock = "ice";
        if (location.getBlock().getType() == Material.PACKED_ICE) getBlock = "ice";

        if (getBlock.equals("stone")) Friction = Friction * 0.6f;
        if (getBlock.equals("ice")) Friction = Friction * 91f;
        if (getBlock.equals("slime")) Friction = Friction * 0.8f;

        return Friction;
    }


}
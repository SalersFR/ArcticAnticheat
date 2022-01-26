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

    public SpeedB(PlayerData data) {
        super(data, "Speed", "B", "movement.speed.b", "Checks if player is following minecraft's strafe movement protocol in ground.", true);
    }
    public static double angle(Vector a, Vector b) {
        double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
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

            double lastMotionX = movementProcessor.getLastDeltaX();
            double lastMotionZ = movementProcessor.getLastDeltaZ();

            //friction
            float friction = 0.91F;
            if (data.getCollisionProcessor().isLastLastClientOnGround()) {
                friction = (float) getFriction(new Location(data.getPlayer().getWorld(), movementProcessor.getLastX()
                        ,movementProcessor.getLastY(),movementProcessor.getLastZ()),collisionProcessor.isLastClientOnGround());
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

            if (collisionProcessor.getClientGroundTicks() > 2 && angle > 0.15
                    && angle < (Math.PI / 4 - 0.05) && rotationProcessor.getDeltaYaw() > 7.5f) {
                if(++buffer > 6) {
                    buffer = 2.5;
                    fail("angle=" + angle);
                }
            } else if(buffer > 0) buffer -= 0.25;
        }


    }


    @SuppressWarnings("deprecation")
    public static double getFriction(Location loc1,boolean onGround) {
        double friction = 0.91;
        String getBlock = "stone";
        Location location = loc1.toVector().subtract(new Vector(0,1,0)).toLocation(loc1.getWorld());

        if (!onGround) getBlock = "air";
        if (location.getBlock().getType() == Material.SLIME_BLOCK) getBlock = "slime";
        if (location.getBlock().getType() == Material.ICE) getBlock = "ice";
        if (location.getBlock().getType() == Material.PACKED_ICE) getBlock = "ice";

        if (getBlock.equals("stone")) friction = friction * 0.6f;
        if (getBlock.equals("ice")) friction = friction * 0.91f;
        if (getBlock.equals("slime")) friction = friction * 0.8f;

        return friction;
    }


}
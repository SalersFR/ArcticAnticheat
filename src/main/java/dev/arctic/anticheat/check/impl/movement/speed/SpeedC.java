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

public class SpeedC extends Check {
    private Vector lastMotion;
    private boolean lastOnGround;
    private boolean lastLastOnGround;
    private double lastMotionX;
    private double lastMotionZ;
    private double buffer;
    private Location lastPos;

    public SpeedC(PlayerData data) {
        super(data, "Speed", "C", "movement.speed.c", "Checks if player is following minecraft's strafe movement protocol in air.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            Vector motion = new Vector(movementProcessor.getDeltaX(),0,movementProcessor.getDeltaZ());

            Vector lastMotion = this.lastMotion;
            this.lastMotion = motion;

            boolean onGround = collisionProcessor.isClientOnGround();
            boolean lastOnGround = collisionProcessor.isLastClientOnGround();
            boolean lastLastOnGround = this.lastLastOnGround;
            this.lastLastOnGround = lastOnGround;

            double friction = 0.91;
            lastMotion.multiply(friction);

            double diff = motion.distance(lastMotion);

            /**  I should recode everything below this **/
            Location loc = movementProcessor.getLocation().toLoc(data.getPlayer().getWorld());
            Location lastLoc = null;
            if (lastPos != null) {
                lastLoc = lastPos;
            } else lastLoc = loc.clone();
            lastPos = loc;

            boolean inBlocks = isInBlocks(loc);
            if (isInBlocks(lastLoc)) inBlocks = true;

            boolean isFlying = data.getPlayer().getAllowFlight(); //TODO add my transaction flying check.
            if (isFlying) return;


            //makes the number easier to read.
            diff*=1000000;
            diff = Math.round(diff);
            diff/=1000;

            //debug
            if (!onGround && !lastOnGround && !lastLastOnGround && !inBlocks &&
                    data.getVelocityProcessor().getVelocityTicks() > 5 && !data.getMovementProcessor().isSlowDown()) {
                if (diff > 26 && diff < 50) {

                    if (++buffer > 8) {
                        fail("diff " + diff);
                    }
                } else if (buffer > 0) buffer-=0.05;
            }
        }
    }

    public boolean isInBlocks(Location location) {
        Location min = location.toVector().add(new Vector(0.6,1,0.6)).toLocation(location.getWorld());
        Location max = location.toVector().subtract(new Vector(0.6,0,0.6)).toLocation(location.getWorld());
        for (Block block : blocksFromTwoPoints(min,max)) {
            if (block.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
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
}

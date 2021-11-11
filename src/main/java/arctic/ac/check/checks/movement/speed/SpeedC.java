package arctic.ac.check.checks.movement.speed;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SpeedC extends Check {

    public SpeedC(PlayerData data) {
        super(data, "Speed", "C", "movement.speed.c", "Checks if player is following minecraft's movement protocol in air.",true);
    }
    private Vector motion = new Vector(0, 0, 0);
    private double airTicks;
    private boolean stillFlying;
    public long lastFlyingTime;
    public double buffer;

    @Override
    public void handle(Event event) {
        if (event instanceof MoveEvent) {
            MoveEvent e = (MoveEvent) event;

            Vector motionXZ = new Vector(e.getDeltaX(),0,e.getDeltaZ());
            Vector lastMotionXZ = motion.clone();
            motion = motionXZ;

            double friction = 0.91;
            lastMotionXZ.multiply(friction);

            boolean onGround = e.isGround();
            if (!onGround) {airTicks++;} else {airTicks = 0;}

            boolean nearBlocks = isNearBlocks(e.getTo().toVector().toLocation(data.getPlayer().getWorld()));
            boolean velocityTaken = data.getVelocityData().getVelocityTicks() < 3;
            boolean attackSlowDown = data.getInteractData().isHasHitSlowDown();
            boolean check = airTicks >= 3; //near blocks
            checkForFlying(data.getPlayer());

            double diff = getDist(motionXZ,lastMotionXZ);

            //isVelocityTaken
            //stillFlying
            if (airTicks > 3 && diff > 26 && !attackSlowDown && !nearBlocks & !velocityTaken) {
                buffer++;
                if (buffer > 4) {
                    fail("diff " + diff);
                }
            } else if (buffer > 0) buffer-=0.05;
        }
    }

    public void checkForFlying(Player player) {
        boolean isFlying = player.isFlying();
        if (isFlying) lastFlyingTime = System.currentTimeMillis();
        stillFlying = System.currentTimeMillis() - lastFlyingTime < 500 + data.getNetworkProcessor().getTransactionPing();
    }

    public boolean isNearBlocks(Location location) {
        Location min = location.toVector().add(new Vector(0.6,1,0.6)).toLocation(location.getWorld());
        Location max = location.toVector().subtract(new Vector(0.6,0,0.6)).toLocation(location.getWorld());
        for (Block block : blocksFromTwoPoints(min,max)) {
            if (block.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public double getDist(Vector from,Vector to) {
        double num = from.distance(to);
        //makes the number easier to read.
        num*=1000000;
        num = Math.round(num);
        num/=1000;
        return num;
    }

    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
}

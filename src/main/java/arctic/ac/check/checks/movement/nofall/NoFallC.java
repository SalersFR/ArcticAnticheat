package arctic.ac.check.checks.movement.nofall;


import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NoFallC extends Check {

    public NoFallC(PlayerData data) {
        super(data, "NoFall", "C", "movement.nofall.c", "Checks if player is spoofing ground state.", false);
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
    public void handle(Event event) {
        if (event instanceof MoveEvent) {
            MoveEvent e = (MoveEvent) event;

            Location min = e.getTo().toVector().subtract(new Vector(0.8, 5, 0.8)).toLocation(data.getPlayer().getWorld());
            Location max = e.getTo().toVector().add(new Vector(0.8, 1, 0.8)).toLocation(data.getPlayer().getWorld());
            boolean isInsideBlocks = false;
            for (Block block : blocksFromTwoPoints(min, max)) {
                if (block.getType() != Material.AIR) {
                    isInsideBlocks = true;
                }
            }

            boolean clientOnGround = e.isGround();
            boolean serverOnGround = isInsideBlocks;

            if (data.getPosData().isTeleporting()) return;

            if (clientOnGround && !serverOnGround) {
                fail("GroundSpoof");
            }
        }
    }
}

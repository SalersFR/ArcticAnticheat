package arctic.ac.data.processor.impl;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import arctic.ac.utils.MathUtils;
import javafx.scene.shape.Arc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WorldTracker {
    private final ArrayList<Block> addBlocks = new ArrayList<>();
    private final PlayerData data;

    public WorldTracker(PlayerData data) {
        this.data = data;
    }

    public void place(Block block) {
        addBlocks.add(block);
        data.getTransactionHandler().preTransaction(new Runnable() {
            @Override
            public void run() {
                addBlocks.remove(block);
            }
        });
    }

    public boolean onGround(Location pos, double xyzOffSet, double upOffSet, double downOffSet, World world) {
        Location min = pos.toVector().subtract(new Vector(xyzOffSet,downOffSet,xyzOffSet)).toLocation(world);
        Location max = pos.toVector().add(new Vector(xyzOffSet,upOffSet,xyzOffSet)).toLocation(world);

        for (Block block : blocksFromTwoPoints(min,max)) {
            boolean onGround = block.getType() != Material.AIR;
            for (Block var1 : addBlocks) {
                Vector var2 = new Vector(var1.getLocation().getX(),var1.getLocation().getY() + 1,var1.getLocation().getZ());
                if (var2.equals(block.getLocation().toVector())) {
                    onGround = true;
                }
            }
            if (onGround) return true;
        }

        return false;
    }

    public List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

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

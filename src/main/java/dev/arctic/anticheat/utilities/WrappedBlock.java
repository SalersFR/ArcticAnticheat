package dev.arctic.anticheat.utilities;


import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Stairs;

import java.util.Locale;

@Getter
public class WrappedBlock {

    private boolean loaded, water, lava, solid, ice, slime, soulSand, web, piston, fence, fenceGate, wall, door, slab, stairs, carpet;
    private String name;
    private Material material;
    private double x, y, z;
    private Location location;

    public WrappedBlock(Block block) {
        loaded = block != null;

        if (loaded) {
            material = block.getType();
            name = material.name();
            x = block.getX();
            y = block.getY();
            z = block.getZ();
            location = new Location(block.getWorld(), x, y, z);

            solid = material.isSolid() || material == Material.SNOW;
            water = material == Material.STATIONARY_WATER
                    || material == Material.WATER;
            lava = material == Material.STATIONARY_LAVA
                    || material == Material.LAVA;
            ice = material == Material.PACKED_ICE
                    || material == Material.ICE;
            slime = material == Material.SLIME_BLOCK;
            soulSand = material == Material.SOUL_SAND;
            web = material == Material.WEB;
            piston = material == Material.PISTON_BASE
                    || material == Material.PISTON_EXTENSION
                    || material == Material.PISTON_MOVING_PIECE
                    || material == Material.PISTON_STICKY_BASE;
            carpet = material == Material.CARPET;
            fence = material == Material.FENCE
                    || material == Material.ACACIA_FENCE
                    || material == Material.BIRCH_FENCE
                    || material == Material.DARK_OAK_FENCE
                    || material == Material.JUNGLE_FENCE
                    || material == Material.NETHER_FENCE
                    || material == Material.SPRUCE_FENCE;
            fenceGate = material == Material.FENCE_GATE
                    || material == Material.ACACIA_FENCE_GATE
                    || material == Material.BIRCH_FENCE_GATE
                    || material == Material.DARK_OAK_FENCE_GATE
                    || material == Material.JUNGLE_FENCE_GATE
                    || material == Material.SPRUCE_FENCE_GATE;
            wall = material == Material.COBBLE_WALL;
            door = material == Material.ACACIA_DOOR
                    || material == Material.BIRCH_DOOR
                    || material == Material.WOOD_DOOR
                    || material == Material.WOODEN_DOOR
                    || material == Material.SPRUCE_DOOR
                    || material == Material.JUNGLE_DOOR
                    || material == Material.DARK_OAK_DOOR;
            slab = block.toString().toLowerCase(Locale.ROOT).contains("slab");
            stairs = block instanceof Stairs;
        }
    }
}

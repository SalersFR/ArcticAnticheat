package dev.arctic.anticheat.utilities;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoundingBox {

    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public BoundingBox(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        } else {
            this.minX = maxX;
            this.maxX = minX;
        }
        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        } else {
            this.minY = maxY;
            this.maxY = minY;
        }
        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        } else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }
    }

    public BoundingBox(final Vector min, final Vector max) {

        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();

        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
    }

    public BoundingBox(final Vector location) {
        this.minX = location.getX() - 0.3D;
        this.minY = location.getY();
        this.minZ = location.getZ() - 0.3D;
        this.maxX = location.getX() + 0.3D;
        this.maxY = location.getY() + 1.8D;
        this.maxZ = location.getZ() + 0.3D;
    }

    public BoundingBox(final Player player) {
        this.minX = player.getLocation().getX() - 0.3D;
        this.minY = player.getLocation().getY();
        this.minZ = player.getLocation().getZ() - 0.3D;
        this.maxX = player.getLocation().getX() + 0.3D;
        this.maxY = player.getLocation().getY() + 1.8D;
        this.maxZ = player.getLocation().getZ() + 0.3D;
    }

    public BoundingBox(Location location) {
        this.minX = location.getX() - 0.3D;
        this.minY = location.getY();
        this.minZ = location.getZ() - 0.3D;
        this.maxX = location.getX() + 0.3D;
        this.maxY = location.getY() + 1.8D;
        this.maxZ = location.getZ() + 0.3D;
    }

    public BoundingBox(ArcticLocation location) {
        this.minX = location.getX() - 0.3D;
        this.minY = location.getY();
        this.minZ = location.getZ() - 0.3D;
        this.maxX = location.getX() + 0.3D;
        this.maxY = location.getY() + 1.8D;
        this.maxZ = location.getZ() + 0.3D;
    }

    public BoundingBox move(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public List<WrappedBlock> getBlocks(Player player) {
        final List<WrappedBlock> blockList = new ArrayList<>();

        World world = player.getWorld();

        if (minY == maxY) {
            for (double x = minX; x <= maxX; x += (maxX - minX)) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    Location location = new Location(world, x, minY, z);

                    blockList.add(new WrappedBlock(location.getBlock()));
                }
            }
        } else {
            for (double x = minX; x <= maxX; x += (maxX - minX)) {
                for (double y = minY; y <= maxY; y += (maxY - minY)) {
                    for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                        Location location = new Location(world, x, y, z);

                        blockList.add(new WrappedBlock(location.getBlock()));
                    }
                }
            }
        }

        blockList.removeIf(block -> !block.isLoaded());

        return blockList;
    }

    public BoundingBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox expandSpecific(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;

        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;

        return this;
    }

    // Copied from MCP 1.8.8
    public BoundingBox union(final BoundingBox other) {
        final double minX = Math.min(this.minX, other.minX);
        final double minY = Math.min(this.minY, other.minY);
        final double minZ = Math.min(this.minZ, other.minZ);
        final double maxX = Math.max(this.maxX, other.maxX);
        final double maxY = Math.max(this.maxY, other.maxY);
        final double maxZ = Math.max(this.maxZ, other.maxZ);

        return new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public double getSize() {
        final Vector min = new Vector(minX, minY, minZ);
        final Vector max = new Vector(maxX, maxY, maxZ);

        return min.distance(max);
    }


    public double min(int i) {
        switch (i) {
            case 0:
                return minX;
            case 1:
                return minY;
            case 2:
                return minZ;
            default:
                return 0;
        }
    }

    public double max(int i) {
        switch (i) {
            case 0:
                return maxX;
            case 1:
                return maxY;
            case 2:
                return maxZ;
            default:
                return 0;
        }
    }



}
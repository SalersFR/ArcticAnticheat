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
    // from hawk
    public Vector[] getVertices() {
        return new Vector[]{new Vector(minX, minY, minZ),
                new Vector(minX, minY, maxZ),
                new Vector(minX, maxY, minZ),
                new Vector(minX, maxY, maxZ),
                new Vector(maxX, minY, minZ),
                new Vector(maxX, minY, maxZ),
                new Vector(maxX, maxY, minZ),
                new Vector(maxX, maxY, maxZ)};
    }
    
    // From hawk
    public boolean betweenRays(Vector pos, Vector dir1, Vector dir2) {
        if(dir1.dot(dir2) > 0.999) {
            return this.intersectsRay(new RayTrace(pos, dir2), 0, Float.MAX_VALUE) != null;
        }
        else {
            Vector planeNormal = dir2.clone().crossProduct(dir1);
            Vector[] vertices = this.getVertices();
            boolean hitPlane = false;
            boolean above = false;
            boolean below = false;
            for(Vector vertex : vertices) {
                vertex.subtract(pos);

                if(!hitPlane) {
                    if (vertex.dot(planeNormal) > 0) {
                        above = true;
                    } else {
                        below = true;
                    }
                    if (above && below) {
                        hitPlane = true;
                    }
                }
            }
            if(!hitPlane) {
                return false;
            }

            Vector extraDirToDirNormal = planeNormal.clone().crossProduct(dir2);
            Vector dirToExtraDirNormal = dir1.clone().crossProduct(planeNormal);
            boolean betweenVectors = false;
            boolean frontOfExtraDirToDir = false;
            boolean frontOfDirToExtraDir = false;
            for(Vector vertex : vertices) {
                if(!frontOfExtraDirToDir && vertex.dot(extraDirToDirNormal) >= 0) {
                    frontOfExtraDirToDir = true;
                }
                if(!frontOfDirToExtraDir && vertex.dot(dirToExtraDirNormal) >= 0) {
                    frontOfDirToExtraDir = true;
                }

                if(frontOfExtraDirToDir && frontOfDirToExtraDir) {
                    betweenVectors = true;
                    break;
                }
            }
            return betweenVectors;
        }
    }

    // from hawk
    public Vector intersectsRay(RayTrace ray, float minDist, float maxDist) {
        Vector invDir = new Vector(1f / ray.direction.getX(), 1f / ray.direction.getY(), 1f / ray.direction.getZ());

        boolean signDirX = invDir.getX() < 0;
        boolean signDirY = invDir.getY() < 0;
        boolean signDirZ = invDir.getZ() < 0;

        Vector bbox = signDirX ? new Vector(maxX, maxY, maxZ) : new Vector(minX, maxY, maxZ);
        double tmin = (bbox.getX() - ray.origin.getX()) * invDir.getX();
        bbox = signDirX ? new Vector(minX, maxY, maxZ) : new Vector(maxX, maxY, maxZ);
        double tmax = (bbox.getX() - ray.origin.getX()) * invDir.getX();
        bbox = signDirY ? new Vector(maxX, maxY, maxZ) : new Vector(minX, maxY, maxZ);
        double tymin = (bbox.getY() - ray.origin.getY()) * invDir.getY();
        bbox = signDirY ? new Vector(minX, maxY, maxZ) : new Vector(maxX, maxY, maxZ);
        double tymax = (bbox.getY() - ray.origin.getY()) * invDir.getY();

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? new Vector(maxX, maxY, maxZ) : new Vector(minX, maxY, maxZ);
        double tzmin = (bbox.getZ() - ray.origin.getZ()) * invDir.getZ();
        bbox = signDirZ ? new Vector(minX, maxY, maxZ) : new Vector(maxX, maxY, maxZ);
        double tzmax = (bbox.getZ() - ray.origin.getZ()) * invDir.getZ();

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public BoundingBox(final Vector min, final Vector max) {

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
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
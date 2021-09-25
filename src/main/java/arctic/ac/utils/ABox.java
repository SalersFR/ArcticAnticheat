package arctic.ac.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class ABox {
    //credits to somewhere lol, but
    private double x1, x2, y1, y2, z1, z2;

    public ABox(final ALocation location) {
        this(location.getX(), location.getY(), location.getZ());
    }

    public ABox(final double x, final double y, final double z) {
        this(x, x, y, y, z, z);
    }

    public ABox(final Vector data) {
        this(data.getX() - 0.4D,
                data.getY(),
                data.getZ() - 0.4D,
                data.getX() + 0.4D,
                data.getY() + 1.9D,
                data.getZ() + 0.4D);
    }

    public ABox(final ALocation PLocation, final ALocation PLocation2) {
        this(Math.min(PLocation.getX(), PLocation2.getX()), Math.max(PLocation.getX(), PLocation2.getX()), Math.min(PLocation.getY(), PLocation2.getY()), Math.max(PLocation.getY(), PLocation2.getY()), Math.min(PLocation.getZ(), PLocation2.getZ()), Math.max(PLocation.getZ(), PLocation2.getZ()));
    }

    public ABox(final double x1, final double x2, final double y1, final double y2, final double z1, final double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    public static boolean checkBlocks(final Collection<Block> blocks, final Predicate<Material> predicate) {
        final Iterator<Block> var2 = blocks.iterator();
        do {
            if (var2.hasNext()) continue;
            return true;
        } while (predicate.test((var2.next()).getType()));
        return false;
    }

    public ABox add(final ABox other) {
        this.x1 += other.x1;
        this.x2 += other.x2;
        this.y1 += other.y1;
        this.y2 += other.y2;
        this.z1 += other.z1;
        this.z2 += other.z2;
        return this;
    }

    public ABox move(final double x, final double y, final double z) {
        this.x1 += x;
        this.x2 += x;
        this.y1 += y;
        this.y2 += y;
        this.z1 += z;
        this.z2 += z;
        return this;
    }

    public ABox expand(final double x, final double y, final double z) {
        this.x1 -= x;
        this.x2 += x;
        this.y1 -= y;
        this.y2 += y;
        this.z1 -= z;
        this.z2 += z;
        return this;
    }

    public List<Block> getBlocks(final World world) {
        final int x1 = (int) Math.floor(this.x1);
        final int x2 = (int) Math.ceil(this.x2);
        final int y1 = (int) Math.floor(this.y1);
        final int y2 = (int) Math.ceil(this.y2);
        final int z1 = (int) Math.floor(this.z1);
        final int z2 = (int) Math.ceil(this.z2);
        final ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(world.getBlockAt(x1, y1, z1));
        for (int x = x1; x < x2; ++x) {
            for (int y = y1; y < y2; ++y) {
                for (int z = z1; z < z2; ++z) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public boolean contains(final ALocation location) {
        return this.x1 <= location.getX() && this.x2 >= location.getX() && this.y1 <= location.getY() && this.y2 >= location.getY() && this.z1 <= location.getZ() && this.z2 >= location.getZ();
    }

    public double distanceXZ(final double x, final double z) {
        final double dx = Math.min(Math.pow(x - this.x1, 2.0), Math.pow(x - this.x2, 2.0));
        final double dz = Math.min(Math.pow(z - this.z1, 2.0), Math.pow(z - this.z2, 2.0));
        return Math.sqrt(dx + dz);
    }

    public boolean checkBlocks(final World world, final Predicate<Material> predicate) {
        return ABox.checkBlocks(this.getBlocks(world), predicate);
    }

    public double cX() {
        return (this.x1 + this.x2) * 0.5;
    }

    public double cY() {
        return (this.y1 + this.y2) * 0.5;
    }

    public double cZ() {
        return (this.z1 + this.z2) * 0.5;
    }

    public double distanceXZ(final ALocation loc) {

        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();

        final double dx = Math.min(Math.pow(x - this.x1, 2.0), Math.pow(x - this.x2, 2.0));
        final double dz = Math.min(Math.pow(z - this.z1, 2.0), Math.pow(z - this.z2, 2.0));
        final double dy = Math.min(Math.pow(y - this.y1, 2.0), Math.pow(y - this.y2, 2.0));
        return Math.sqrt(dx + dy + dz);
    }
}
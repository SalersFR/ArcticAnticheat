package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.protocol.wrappers.BlockPosition;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.BoundingBox;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ScaffoldD extends Check {
    public ScaffoldD(PlayerData data) {
        super(data, "Scaffold", "D", "player.scaffold.d", "Checks for placing a block while not looking at it.", true);
    }

    public static Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        final float rotX = (float)Math.toRadians(yaw);
        final float rotY = (float)Math.toRadians(pitch);
        vector.setY(-Math.sin(rotY));
        final double xz = Math.cos(rotY);
        vector.setX(-xz * Math.sin(rotX));
        vector.setZ(xz * Math.cos(rotX));
        return vector;
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isBlockPlace()) {
            Vector add = new Vector(0, 0, 0);
            add.setY(data.getActionProcessor().isSneaking() ? 1.54 : 1.62);
            Vector pos = data.getPlayer().getLocation().clone().add(add).toVector();
            Vector dir = getDirection(data.getRotationProcessor().getYaw(), data.getRotationProcessor().getPitch());
            Vector extraDir = getDirection(data.getRotationProcessor().getYaw() + data.getRotationProcessor().getDeltaYaw(),  data.getRotationProcessor().getPitch() +  data.getRotationProcessor().getDeltaPitch());
            final BlockPosition blockpos = packet.getBlockPositionModifier().read(0);
            final Location blockLocation = new Location(
                    data.getPlayer().getWorld(),
                    blockpos.getX(),
                    blockpos.getY(),
                    blockpos.getZ()
            );
            Vector min = blockLocation.toVector();
            Vector max = blockLocation.toVector().clone().add(new Vector(1, 1, 1));
            BoundingBox targetBB = new BoundingBox(min, max);

            if(!targetBB.betweenRays(pos, dir, extraDir) && !isRight(min, max)) {
                fail("AAAB Not between rays, Min: " + min + " Max: " + max);
            }
        }
    }

    public boolean isRight(Vector min, Vector max) {
        return (min.getX() + 1) == max.getX() && (min.getY() + 1) == max.getY() && (min.getZ() + 1) == max.getZ();
    }
}

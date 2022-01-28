package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.protocol.wrappers.BlockPosition;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.util.Vector;

public class ScaffoldD extends Check {

    public ScaffoldD(PlayerData data) {
        super(data, "Scaffold", "D", "player.scaffold.d", "Checks for placing a block while not looking at it.", true);
    }

    private Packet packet = null;

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isBlockPlace()) {
            this.packet = packet;
        } else if(packet.isFlying() && !packet.isFlyingFR()) {
            if(this.packet != null) {
                final Vector eyeLocation = data.getPlayer().getEyeLocation().toVector();
                final BlockPosition pos = this.packet.getBlockPositionModifier().read(0);

                WrapperPlayClientBlockPlace wrapped = new WrapperPlayClientBlockPlace(this.packet);
                if(wrapped.getFace() == 255) {
                    return;
                }

                final Vector blockLocation = new Vector(pos.getX(), pos.getY(), pos.getZ());

                final Vector directionToDestination = blockLocation.clone().subtract(eyeLocation);
                final Vector playerDirection = data.getPlayer().getEyeLocation().getDirection();

                final float angle = directionToDestination.angle(playerDirection);
                final float distance = (float) eyeLocation.distance(blockLocation);

                final boolean exempt = blockLocation.getX() == -1.0 && blockLocation.getY() == -1.0 && blockLocation.getZ() == -1.0;
                final boolean invalid = angle > 1.0F && distance > 1.5;

                if (invalid && !exempt) {
                    fail("Angle: " + angle);
                }
            }
            this.packet = null;
        }
    }

    public boolean isRight(Vector min, Vector max) {
        return (min.getX() + 1) == max.getX() && (min.getY() + 1) == max.getY() && (min.getZ() + 1) == max.getZ();
    }
}

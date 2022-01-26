package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.Location;

public class ScaffoldC extends Check {
    public ScaffoldC(PlayerData data) {
        super(data, "Scaffold", "C", "player.scaffold.c", "Checks for invalid placement", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isBlockPlace()) {
            final Location eyeLocation = data.getPlayer().getEyeLocation();
            final BlockPosition pos = packet.getBlockPositionModifier().read(0);
            final Location blockAgainstLocation = getBlockAgainst(packet.getDirections().read(0), new Location(null, pos.getX(), pos.getY(), pos.getZ()));
            final boolean validInteraction = interactedCorrectly(blockAgainstLocation, eyeLocation, packet.getDirections().read(0));
            if (!validInteraction) {
                EnumWrappers.Direction dir = packet.getDirections().read(0);
                fail("Invalid interact, Direction: " + dir.name());
            }
        }
    }

    private Location getBlockAgainst(final EnumWrappers.Direction direction, final Location blockLocation) {
        if (EnumWrappers.Direction.UP.equals(direction)) {
            return blockLocation.clone().add(0, -1, 0);
        } else if (EnumWrappers.Direction.DOWN.equals(direction)) {
            return blockLocation.clone().add(0, 1, 0);
        } else if (EnumWrappers.Direction.EAST.equals(direction) || EnumWrappers.Direction.SOUTH.equals(direction)) {
            return blockLocation;
        } else if (EnumWrappers.Direction.WEST.equals(direction)) {
            return blockLocation.clone().add(1, 0, 0);
        } else if (EnumWrappers.Direction.NORTH.equals(direction)) {
            return blockLocation.clone().add(0, 0, 1);
        }
        return null;
    }

    private boolean interactedCorrectly(Location block, Location player, EnumWrappers.Direction face) {
        if (EnumWrappers.Direction.UP.equals(face)) {
            return player.getY() > block.getY();
        } else if (EnumWrappers.Direction.DOWN.equals(face)) {
            return player.getY() < block.getY();
        } else if (EnumWrappers.Direction.WEST.equals(face)) {
            return player.getX() < block.getX();
        } else if (EnumWrappers.Direction.EAST.equals(face)) {
            return player.getX() > block.getX();
        } else if (EnumWrappers.Direction.NORTH.equals(face)) {
            return player.getZ() < block.getZ();
        } else if (EnumWrappers.Direction.SOUTH.equals(face)) {
            return player.getZ() > block.getZ();
        }
        return true;
    }
}

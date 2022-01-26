package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.ServerUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

@Getter
public class ActionProcessor extends Processor {

    private boolean sprinting, sneaking, eating, placementUnder;

    private Location placementLocation, lastPlacementLocation;
    private int ticksSincePlace;

    public ActionProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isFlying()) {
            if (!data.getPlayer().getItemInHand().getType().isEdible()) eating = false;
            ++ticksSincePlace;
            if(ticksSincePlace > 7) {
                placementUnder = false;
            }
        }
        if(event.getPacket().isBlockPlace()) {
            final WrapperPlayClientBlockPlace wrapper = new WrapperPlayClientBlockPlace(event.getPacket());
            final BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
            if(wrapper.getFace() == 255) {
                return;
            }
            ticksSincePlace = 0;
            Location loc = new Location(data.getPlayer().getWorld(), pos.getX(), pos.getY(), pos.getZ());
            if(lastPlacementLocation == null || placementLocation == null) {
                this.placementLocation = loc;
                this.lastPlacementLocation = placementLocation;
                return;
            }
            if(placementUnder(lastPlacementLocation)) {
                this.lastPlacementLocation = loc;
                placementLocation = loc;
                placementUnder = true;
            }
        }
        if (event.getPacket().isEntityAction()) {
            final WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event.getPacket());
            switch (packet.getAction()) {
                case START_SPRINTING:
                    sprinting = true;
                    break;
                case STOP_SPRINTING:
                    sprinting = false;
                    break;
                case START_SNEAKING:
                    sneaking = true;
                    break;
                case STOP_SNEAKING:
                    sneaking = false;
                    break;
            }
        }
        if(event.getPacket().isBlockPlace()) {
            if (data.getPlayer().getItemInHand().getType().isEdible()) eating = true;
        }
        if(event.getPacket().isBlockDig()) {
            final WrapperPlayClientBlockDig packet = new WrapperPlayClientBlockDig(event.getPacket());
            if(packet.getStatus() == EnumWrappers.PlayerDigType.RELEASE_USE_ITEM) {
                eating = false;
            }
        }
    }

    private boolean placementUnder(final Location blockLocation) {
        final double x = data.getMovementProcessor().getX();
        final double y = data.getMovementProcessor().getY();
        final double z = data.getMovementProcessor().getZ();

        final double blockX = blockLocation.getX();
        final double blockY = blockLocation.getY();
        final double blockZ = blockLocation.getZ();

        final double lastBlockY = lastPlacementLocation.getY();

        return Math.floor(y - 0.25) == blockY
                && blockY < y
                && lastBlockY < y
                && lastBlockY < blockY
                && Math.abs(x - blockX) <= 0.8
                && Math.abs(z - blockZ) <= 0.8;
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

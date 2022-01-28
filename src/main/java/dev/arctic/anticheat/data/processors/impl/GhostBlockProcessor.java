package dev.arctic.anticheat.data.processors.impl;

import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.PlayerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class GhostBlockProcessor extends Processor {

    private boolean onGhostBlock, yGround, lastYGround;

    private long setBackTicks;

    private int ghostTicks, buffer;

    public GhostBlockProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isFlying()) {
            onGhostBlock = false;

            if(isOnBoat(data) || data.getCollisionProcessor().isInVehicle() || data.getCollisionProcessor().isOnClimbable() || data.getCollisionProcessor().isInWater() || data.getCollisionProcessor().isInLava() || data.getCollisionProcessor().isOnSlime() || data.getCollisionProcessor().isLastOnSlime() || data.getCollisionProcessor().isTeleporting() || data.getVelocityProcessor().getVelocityTicks() == 0) {
                return;
            }

            final boolean isBridingUp = data.getActionProcessor().isPlacementUnder() && data.getMovementProcessor().getDeltaY() > 0.0;

            final boolean onGhostBlock = data.getCollisionProcessor().isCollisionOnGround() && data.getMovementProcessor().getY() % 0.015625 < 0.03 && data.getCollisionProcessor().getMathAirTicks() > 0;

            final double deltaY = data.getMovementProcessor().getDeltaY();
            final double lastDeltaY = data.getMovementProcessor().getLastDeltaY();

            final int airTicks = data.getCollisionProcessor().getMathAirTicks();


            double predictedY = (lastDeltaY - 0.08) * 0.98F;
            if (Math.abs(predictedY) < 0.005) predictedY = 0.0;

            final boolean underGhostBlock = !data.getCollisionProcessor().isBonkingHead()
                    && Math.abs(deltaY - ((-0.08) * 0.98F)) < 1E-5
                    && Math.abs(deltaY - predictedY) > 1E-5;

            this.onGhostBlock = onGhostBlock || underGhostBlock;

            if (onGhostBlock && airTicks > 5 && !isBridingUp) {
                data.getCancelProcessor().dragDown();
                data.getPlayer().sendMessage("Lagged Back for ghost blocks [EXPERIMENTAL]");
            }
        }
    }

    public boolean isOnBoat(PlayerData user) {
        double offset = user.getMovementProcessor().getY() % 0.015625;
        if ((user.getCollisionProcessor().isClientOnGround() && offset > 0 && offset < 0.009)) {
            return getEntitiesWithinRadius(user.getPlayer().getLocation(), 2).stream()
                    .anyMatch(entity -> entity.getType() == EntityType.BOAT);
        }

        return false;
    }

    /**
     * Bukkit's getNearbyEntities method looks for all entities in all chunks
     * This is a lighter method and can also be used Asynchronously since we won't load any chunks
     *
     * @param location The location to scan for nearby entities
     * @param radius   The radius to expand
     * @return The entities within that radius
     * @author Nik
     */
    public List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {
        final double expander = 16.0D;

        final double x = location.getX();
        final double z = location.getZ();

        final int minX = (int) Math.floor((x - radius) / expander);
        final int maxX = (int) Math.floor((x + radius) / expander);

        final int minZ = (int) Math.floor((z - radius) / expander);
        final int maxZ = (int) Math.floor((z + radius) / expander);

        final World world = location.getWorld();

        final List<Entity> entities = new LinkedList<>();

        try {
            for (int xVal = minX; xVal <= maxX; xVal++) {

                for (int zVal = minZ; zVal <= maxZ; zVal++) {

                    if (!world.isChunkLoaded(xVal, zVal)) continue;
                    if(world.getChunkAt(xVal, zVal) == null) {
                        continue;
                    }
                    for (final Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {

                        if (entity == null) continue;


                        if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                        entities.add(entity);
                    }
                }
            }
        } catch(NoSuchElementException ignored) {

        }

        return entities;
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

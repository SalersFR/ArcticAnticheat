package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.*;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.NumberConversions;

import java.util.List;

@Getter
public class CollisionProcessor extends Processor {

    private BoundingBox boundingBox, bonkingBoundingBox, fenceBoundingBox;

    private List<Entity> entityCollisions;

    private List<WrappedBlock> blockCollisions, bonkingCollisions, fenceCollisions;

    private boolean clientOnGround, mathOnGround, collisionOnGround,
            onIce, onSlime, onSoulSand, onClimbable,
            inWeb, inWater, inLava, inVehicle,
            nearVehicle, nearBoat, nearPiston, nearCarpet, bonkingHead, teleporting, placing,
            lastClientOnGround, lastMathOnGround, lastCollisionOnGround,
            lastOnIce, lastOnSlime, lastOnSoulSand, lastOnClimbable, lastNearPiston,
            lastNearCarpet, lastInWeb, lastInWater, lastInLava, lastInVehicle, nearSlab, nearStairs,
            lastNearVehicle, lastNearBoat, lastBonkingHead, lastTeleporting,
            lastOnGroundSlime, lastOnGroundIce, lastPlacing, lastNearSlab, lastNearStairs;

    private ArcticLocation teleportLocation;
    private boolean sentTeleport;

    private int collisionAirTicks, clientAirTicks, mathAirTicks, collisionGroundTicks, clientGroundTicks, mathGroundTicks, placingTicks, waterTicks;



    public CollisionProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {


            final ArcticLocation location = data.getMovementProcessor().getLocation();

            final Location bukkitLocation = location.toLoc(data.getPlayer().getWorld());


            data.getTransactionProcessor().todoTransaction(() -> {
                boundingBox = new BoundingBox(location).expand(0, 0.01, 0);

                blockCollisions = boundingBox.getBlocks(data.getPlayer());

                bonkingBoundingBox = new BoundingBox(location).expandSpecific(0, 0, -1.81, 0.01, 0, 0);

                bonkingCollisions = bonkingBoundingBox.getBlocks(data.getPlayer());

                fenceBoundingBox = new BoundingBox(location).expandSpecific(0, 0, 0.61, -2.41, 0, 0);

                fenceCollisions = fenceBoundingBox.getBlocks(data.getPlayer());
            });

            updateLastVariables();

            double x = data.getMovementProcessor().getX();
            double y = data.getMovementProcessor().getY();
            double z = data.getMovementProcessor().getZ();

            if (event.getPacket().isPosition()) {
                final WrapperPlayClientPosition packet = new WrapperPlayClientPosition(event.getPacket());
                clientOnGround = packet.getOnGround();
            } else if (event.getPacket().isPosLook()) {
                final WrapperPlayClientPositionLook packet = new WrapperPlayClientPositionLook(event.getPacket());
                clientOnGround = packet.getOnGround();
            } else if (event.getPacket().isLook()) {
                final WrapperPlayClientLook packet = new WrapperPlayClientLook(event.getPacket());
                clientOnGround = packet.getOnGround();
            } else {
                final WrapperPlayClientFlying packet = new WrapperPlayClientFlying(event.getPacket());
                clientOnGround = packet.getOnGround();
            }
            // All blocks in minecraft have a y divisible by 1/64
            mathOnGround = data.getMovementProcessor().getY() % 0.015625 == 0;
            // Not sure if I have to do <= instead of just < but it doesn't really matter
            collisionOnGround = blockCollisions.stream().anyMatch(block -> block.isSolid() && block.getY() <= y)
                    || fenceCollisions.stream().anyMatch(block -> block.isFence() || block.isFenceGate() || block.isWall());
            onIce = blockCollisions.stream().anyMatch(block -> block.isIce() && block.getY() <= y);
            onSlime = blockCollisions.stream().anyMatch(block -> block.isSlime() && block.getY() <= y);
            onSoulSand = blockCollisions.stream().anyMatch(block -> block.isSoulSand() && block.getY() <= y);
            inWeb = blockCollisions.stream().anyMatch(WrappedBlock::isWeb);
            inWater = blockCollisions.stream().anyMatch(WrappedBlock::isWater) && data.getPlayer().getLocation().getBlock().isLiquid();
            inLava = blockCollisions.stream().anyMatch(WrappedBlock::isLava) && data.getPlayer().getLocation().getBlock().isLiquid();
            nearPiston = blockCollisions.stream().anyMatch(WrappedBlock::isPiston);
            nearCarpet = blockCollisions.stream().anyMatch(WrappedBlock::isCarpet);
            nearSlab = blockCollisions.stream().anyMatch(WrappedBlock::isSlab);
            nearStairs = blockCollisions.stream().anyMatch(WrappedBlock::isStairs);

            int flooredX = NumberConversions.floor(x);
            int flooredY = NumberConversions.floor(y);
            int flooredZ = NumberConversions.floor(z);

            Block climbableBlock = ServerUtil.getBlock(new Location(data.getPlayer().getWorld(), flooredX, flooredY, flooredZ));

            entityCollisions = ServerUtil.getEntitiesWithinRadius(
                    data.getPlayer(), location.toLoc(data.getPlayer().getWorld()),
                    0.45 + ((data.getConnectionProcessor().getKeepAlivePing() / 50) * 0.35)
            );

            onClimbable = climbableBlock != null && (climbableBlock.getType() == Material.LADDER
                    || climbableBlock.getType() == Material.VINE);

            nearVehicle = entityCollisions.stream().anyMatch(entity -> entity instanceof Vehicle);
            nearBoat = entityCollisions.stream().anyMatch(entity -> entity instanceof Boat);

            bonkingHead = bonkingCollisions.stream().anyMatch(WrappedBlock::isSolid) || LocationUtils.blockNearHead(bukkitLocation)
                    || LocationUtils.haveABlockNearHead(data.getPlayer());

            teleporting = placing =  false;

            if (sentTeleport
                    && teleportLocation != null
                    && data.getMovementProcessor().getLocation().toBukkitVec().distance(teleportLocation.toBukkitVec()) <= 0.001D) {

                data.getTransactionProcessor().todoTransaction(this::updateTeleport);

            }

            if (lastClientOnGround) {
                lastOnGroundSlime = lastOnSlime;
                lastOnGroundIce = lastOnIce;
            }


            if (collisionOnGround) {
                collisionAirTicks = 0;
                collisionGroundTicks++;
            } else {
                collisionAirTicks++;
                collisionGroundTicks = 0;
            }

            if (clientOnGround) {
                clientAirTicks = 0;
                clientGroundTicks++;
            } else {
                clientAirTicks++;
                clientGroundTicks = 0;
            }

            if (mathOnGround) {
                mathAirTicks = 0;
                mathGroundTicks++;
            } else {
                mathAirTicks++;
                mathGroundTicks = 0;
            }

            if(placing)
                placingTicks = 0;

            placingTicks++;

            if(isInWater()) waterTicks++;
            else waterTicks = 0;


        } else if (event.getPacket().isUseEntity()) {
            final WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
            if (packet.getType() == EnumWrappers.EntityUseAction.INTERACT && packet.getTarget(data.getPlayer().getWorld()) instanceof Vehicle) {
                inVehicle = !inVehicle;
            }
        } else if(event.getPacket().isBlockPlace()) {
            placing = !placing;
        }
    }

    private void updateLastVariables() {
        lastClientOnGround = clientOnGround;
        lastMathOnGround = mathOnGround;
        lastCollisionOnGround = collisionOnGround;
        lastOnIce = onIce;
        lastOnSlime = onSlime;
        lastOnSoulSand = onSoulSand;
        lastOnClimbable = onClimbable;
        lastNearPiston = nearPiston;
        lastInWeb = inWeb;
        lastInWater = inWater;
        lastInLava = inLava;
        lastInVehicle = inVehicle;
        lastNearVehicle = nearVehicle;
        lastNearBoat = nearBoat;
        lastBonkingHead = bonkingHead;
        lastTeleporting = teleporting;
        lastPlacing = placing;
        lastNearSlab = nearSlab;
        lastNearStairs = nearStairs;
        lastNearCarpet = nearCarpet;
    }

    @Override
    public void handleSending(PacketEvent event) {
        if (event.getPacket().isOutPosition()) {
            final WrapperPlayServerPosition pos = new WrapperPlayServerPosition(event.getPacket());

            teleportLocation = new ArcticLocation(pos.getX(), pos.getY(), pos.getZ());

            sentTeleport = true;
        }

    }

    private void updateTeleport() {
        teleporting = true;
        sentTeleport = false;
    }
}

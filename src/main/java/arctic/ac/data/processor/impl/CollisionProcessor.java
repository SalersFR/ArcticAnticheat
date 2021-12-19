package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.WorldUtils;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class CollisionProcessor extends Processor {

    private int collisionAirTicks, clientAirTicks, mathAirTicks, collisionGroundTicks, clientGroundTicks, mathGroundTicks, slimeTicks, iceTicks;

    private boolean collisionGround, clientGround, mathGround, web, climbable, liquid, nearBoat, blockNearHead;

    public CollisionProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if (event.getPacketType() == PacketType.IN_POSITION || event.getPacketType() == PacketType.IN_POSITION_LOOK) {
            final WrappedInFlying wrapper = new WrappedInFlying(event.getPacket());

            final Location location = new Location(getData().getPlayer().getWorld(), getData().getMovementProcessor()
                    .getX(), getData().getMovementProcessor().getY(), getData().getMovementProcessor().getZ());

            final WorldUtils worldUtils = new WorldUtils();

            collisionGround = worldUtils.isOnGround(location, -0.00001);
            clientGround = wrapper.isOnGround(); //player.isOnGround()
            mathGround = (getData().getPlayer().getLocation().getY() % (1 / 64)) <= .0001D;

            if (collisionGround) {
                collisionAirTicks = 0;
                collisionGroundTicks++;
            } else {
                collisionAirTicks++;
                collisionGroundTicks = 0;
            }

            if (clientGround) {
                clientAirTicks = 0;
                clientGroundTicks++;
            } else {
                clientAirTicks++;
                clientGroundTicks = 0;
            }

            if (mathGround) {
                mathAirTicks = 0;
                mathGroundTicks++;
            } else {
                mathAirTicks++;
                mathGroundTicks = 0;
            }

            if (worldUtils.isOnACertainBlock(getData().getPlayer(), "slime"))
                slimeTicks = 0;
            else
                slimeTicks++;

            if (worldUtils.isOnACertainBlock(getData().getPlayer(), "ice"))
                iceTicks = 0;
            else
                iceTicks++;

            web = worldUtils.isCollidingWithWeb(getData().getPlayer());
            liquid = worldUtils.isInLiquid(getData().getPlayer());
            climbable = worldUtils.isCollidingWithClimbable(getData().getPlayer());
            nearBoat = worldUtils.isNearBoat(getData().getPlayer());
            blockNearHead = worldUtils.blockNearHead(location) || worldUtils.haveABlockNearHead(getData().getPlayer());
        }
    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {

    }
}

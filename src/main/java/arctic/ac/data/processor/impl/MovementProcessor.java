package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import eu.salers.salty.packet.wrappers.play.out.impl.WrappedOutEntityTeleport;
import lombok.Getter;

@Getter
public class MovementProcessor extends Processor {

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;
    private boolean teleported;
    private long lastTeleport;

    public MovementProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if (event.getPacketType() == PacketType.IN_POSITION || event.getPacketType() == PacketType.IN_POSITION_LOOK) {
            final WrappedInFlying wrapper = new WrappedInFlying(event.getPacket());

            lastX = this.x;
            lastY = this.y;
            lastZ = this.z;

            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();

            lastDeltaX = deltaX;
            lastDeltaY = deltaY;
            lastDeltaZ = deltaZ;
            lastDeltaXZ = deltaXZ;

            deltaX = this.x - lastX;
            deltaY = this.y - lastY;
            deltaZ = this.z - lastZ;
            deltaXZ = Math.hypot(deltaX, deltaZ);

            this.teleported = (System.currentTimeMillis() - lastTeleport) <= 500L;
        }
    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {
        if(event.getPacketType() == PacketType.IN_TELEPORT_ACCEPT) {
            final WrappedOutEntityTeleport wrapped = new WrappedOutEntityTeleport(event.getPacket());
            if(wrapped.getEntityId() == getData().getPlayer().getEntityId())
                this.lastTeleport = System.currentTimeMillis();


        }

    }
}

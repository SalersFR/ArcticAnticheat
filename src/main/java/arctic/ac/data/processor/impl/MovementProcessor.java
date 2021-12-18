package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import eu.salers.salty.packet.wrappers.play.out.impl.WrappedOutPosition;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

@Getter
public class MovementProcessor extends Processor {

    private final List<Vector> teleportVecs = new LinkedList<>();

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;

    private int teleportTicks;

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
            deltaXZ = MathUtils.hypot(deltaX, deltaZ);

            if(!wrapper.isOnGround() && wrapper.isHasPos() && wrapper.isHasLook()) {
                for(final Vector tpVecs : teleportVecs) {
                    if(tpVecs.distance(new Vector(x,y,z)) <= 1.0E-6) {
                        this.teleportTicks = 0;
                        teleportVecs.remove(tpVecs);
                        break;
                    }
                }
            }

            teleportTicks++;


        }
    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {
        if (event.getPacketType() == PacketType.OUT_POSITION) {
            final WrappedOutPosition wrapped = new WrappedOutPosition(event.getPacket());
            this.teleportVecs.add(new Vector(wrapped.getX(), wrapped.getY(), wrapped.getZ()));

        }

    }

    public boolean isTeleported() {
        return teleportTicks <= 5;
    }


}

package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.protocol.PacketType;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.ArcticLocation;
import dev.arctic.anticheat.utilities.MathUtils;
import lombok.Getter;

@Getter

public class MovementProcessor extends Processor {

    private double deltaX, deltaZ, deltaXZ, deltaY, lastDeltaX, lastDeltaY,lastDeltaZ, lastDeltaXZ;
    private double lastX, lastY, lastZ;
    private double x = 0, y = 0, z = 0;
    private boolean pos, lastPos;
    private ArcticLocation location = new ArcticLocation(x,y,z);


    public MovementProcessor(PlayerData data) {
        super(data);
    }


    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {



            /*
             * Getting the X one tick ago
             * And setting the deltaX with the current X and last X
             **/
            this.lastX = this.x;

            /*
             * Getting the Y one tick ago
             * And setting the deltaY with the current and last Y
             */
            this.lastY = this.y;

            /*
             * Getting the Z one tick ago
             * And setting the deltaZ with the current and last Z
             */
            this.lastZ = this.z;

            // Update the positions

            updatePos(event);

            /**
             * the deltas one tick ago
             */

            lastDeltaX = deltaX;
            lastDeltaY = deltaY;
            lastDeltaZ = deltaZ;

            lastDeltaXZ = deltaXZ;

            // Deltas

            this.deltaX = (this.x - this.lastX);
            this.deltaY = (this.y - this.lastY);
            this.deltaZ = (this.z - this.lastZ);

            //to prevent some issues
            handleOtherFlying(event.getPacket());

            // DeltaXZ

            this.deltaXZ = MathUtils.hypot(deltaX, deltaZ);

            lastPos = pos;
            pos = true;


        } else if(event.getPacket().isFlying()) {
            lastPos = pos;
            pos = false;
        }
    }

    @Override
    public void handleSending(PacketEvent event) {

    }

    public void updatePos(PacketEvent packet) {
        if (packet.getPacket().isPosition()) {
            WrapperPlayClientPosition wrapper = new WrapperPlayClientPosition(packet.getPacket());
            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();
        } else if (packet.getPacket().isPosLook()) {
            WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(packet.getPacket());
            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();
        }

        this.location.setX(x);
        this.location.setY(y);
        this.location.setZ(z);
    }

    public void handleOtherFlying(final Packet packet) {
        if(packet.getType() == PacketType.Play.Client.FLYING || packet.isLook()) {
            deltaX = 0;
            deltaY = 0;
            deltaZ = 0;
        }
    }
}

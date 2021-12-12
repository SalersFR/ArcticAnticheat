package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class RotationProcessor extends Processor {

    private float yaw, pitch,
            deltaYaw, deltaPitch,
            lastYaw, lastPitch,
            lastDeltaYaw, lastDeltaPitch,
            yawAccel, pitchAccel,
            lastYawAccel, lastPitchAccel;
    private double gcdYaw, gcdPitch;
    private long expandedGcdYaw, expandedGcdPitch;
    private int sensitivity, ticksSinceCinematic;


    public RotationProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if(event.getPacketType() == PacketType.IN_LOOK || event.getPacketType() == PacketType.IN_POSITION_LOOK) {
            final WrappedInFlying wrapper = new WrappedInFlying(event.getPacket());

            lastYaw = yaw;
            lastPitch = pitch;

            deltaYaw = Math.abs(yaw % 360 - lastYaw % 360) % 360;
            deltaPitch = Math.abs(pitch - lastPitch);

            this.yaw = wrapper.getYaw();
            this.pitch = wrapper.getPitch();

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;

            lastYawAccel = this.yawAccel;
            yawAccel = Math.abs(deltaYaw - lastDeltaYaw);

            lastPitchAccel = this.pitchAccel;
            pitchAccel = Math.abs(deltaPitch - lastDeltaPitch);

            gcdYaw = MathUtils.getGcd(deltaYaw , lastDeltaYaw);
            gcdPitch = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

            expandedGcdYaw = (long) MathUtils.getGcd(deltaYaw * MathUtils.EXPANDER, lastDeltaYaw * MathUtils.EXPANDER);
            expandedGcdPitch = (long) MathUtils.getGcd(deltaPitch * MathUtils.EXPANDER, lastDeltaPitch * MathUtils.EXPANDER);

            sensitivity = (int) MathUtils.getSensitivity(deltaPitch, lastDeltaPitch);
            handleCinematic();


        }
    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {

    }

    public void handleCinematic() {

        this.ticksSinceCinematic++;

        final double accelAccelYaw = Math.abs(yawAccel - lastYawAccel);
        final double accelAccelPitch = Math.abs(pitchAccel - lastPitchAccel);

        final boolean invalidYaw = accelAccelYaw < .05 && accelAccelYaw > 0;
        final boolean invalidPitch = accelAccelPitch < .05 && accelAccelPitch > 0;

        final boolean exponentialYaw = String.valueOf(accelAccelYaw).contains("E");
        final boolean exponentialPitch = String.valueOf(accelAccelPitch).contains("E");

        if (sensitivity < 100 && (exponentialPitch || exponentialYaw || invalidYaw || invalidPitch)) {
            this.ticksSinceCinematic = 0;

        }



    }





}

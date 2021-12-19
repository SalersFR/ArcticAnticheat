package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import lombok.Getter;

@Getter
public class RotationProcessor extends Processor {

    private float yaw, pitch,
            deltaYaw, deltaPitch,
            lastYaw, lastPitch,
            lastDeltaYaw, lastDeltaPitch,
            yawAccel, pitchAccel,
            lastYawAccel, lastPitchAccel;
    private double gcdYaw, gcdPitch, absGcdPitch, absGcdYaw;
    private long expandedGcdYaw, expandedGcdPitch;
    private int sensitivity, ticksSinceCinematic;


    public RotationProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if (isFlyingPacket(event.getPacketType())) {
            final WrappedInFlying wrapper = new WrappedInFlying(event.getPacket());

            if (wrapper.isHasLook()) {

                lastYaw = this.yaw;
                lastPitch = this.pitch;

                this.yaw = wrapper.getYaw();
                this.pitch = wrapper.getPitch();

                lastDeltaYaw = deltaYaw;
                lastDeltaPitch = deltaPitch;

                deltaYaw = Math.abs(yaw - lastYaw) % 360F;
                deltaPitch = Math.abs(pitch - lastPitch);

                lastYawAccel = this.yawAccel;
                lastPitchAccel = this.pitchAccel;

                yawAccel = Math.abs(deltaYaw - lastDeltaYaw);
                pitchAccel = Math.abs(deltaPitch - lastDeltaPitch);

                gcdYaw = MathUtils.getGcd(deltaYaw, lastDeltaYaw);
                gcdPitch = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

                absGcdYaw = MathUtils.getGcd(Math.abs(deltaYaw), Math.abs(lastDeltaYaw));
                absGcdPitch = MathUtils.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

                expandedGcdYaw = (long) MathUtils.gcd(0x4000, (Math.abs(deltaYaw) * MathUtils.EXPANDER), (Math.abs(lastDeltaYaw) * MathUtils.EXPANDER));
                expandedGcdPitch = (long) MathUtils.gcd(0x4000, (Math.abs(deltaPitch) * MathUtils.EXPANDER), (Math.abs(lastDeltaPitch) * MathUtils.EXPANDER));

                sensitivity = (int) MathUtils.getSensitivity(deltaPitch, lastDeltaPitch);
                handleCinematic();

            } else {

                lastYaw = this.yaw;
                lastPitch = this.pitch;

                this.yaw = wrapper.getYaw();
                this.pitch = wrapper.getPitch();

                lastDeltaYaw = deltaYaw;
                lastDeltaPitch = deltaPitch;

                deltaYaw = 0;
                deltaPitch = 0;

                lastYawAccel = this.yawAccel;
                lastPitchAccel = this.pitchAccel;

                yawAccel = Math.abs(deltaYaw - lastDeltaYaw);
                pitchAccel = Math.abs(deltaPitch - lastDeltaPitch);

                gcdYaw = MathUtils.getGcd(deltaYaw, lastDeltaYaw);
                gcdPitch = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

                absGcdYaw = MathUtils.getGcd(Math.abs(deltaYaw), Math.abs(lastDeltaYaw));
                absGcdPitch = MathUtils.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

                expandedGcdYaw = (long) MathUtils.gcd(0x4000, (Math.abs(deltaYaw) * MathUtils.EXPANDER), (Math.abs(lastDeltaYaw) * MathUtils.EXPANDER));
                expandedGcdPitch = (long) MathUtils.gcd(0x4000, (Math.abs(deltaPitch) * MathUtils.EXPANDER), (Math.abs(lastDeltaPitch) * MathUtils.EXPANDER));

                sensitivity = (int) MathUtils.getSensitivity(deltaPitch, lastDeltaPitch);
                handleCinematic();
            }


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

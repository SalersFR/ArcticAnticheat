package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientLook;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.protocol.PacketType;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.MathUtils;
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
    public void handleReceive(PacketEvent event) {

        if (event.getPacket().isFlying()) {

            lastYaw = this.yaw;
            lastPitch = this.pitch;

            updateRot(event.getPacket());

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;

            deltaYaw = Math.abs(yaw - lastYaw) % 360F;
            deltaPitch = Math.abs(pitch - lastPitch);

            handleOtherFlying(event.getPacket());

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

            this.sensitivity = (int) MathUtils.getSensitivity(deltaPitch, lastDeltaPitch);

            handleCinematic();

        }

    }

    private void updateRot(final Packet packet) {
        if (packet.isPosLook()) {
            final WrapperPlayClientPositionLook wrapped = new WrapperPlayClientPositionLook(packet);
            this.yaw = wrapped.getYaw();
            this.pitch = wrapped.getPitch();
        } else if(packet.isLook()) {
            final WrapperPlayClientLook wrapped = new WrapperPlayClientLook(packet);
            this.yaw = wrapped.getYaw();
            this.pitch = wrapped.getPitch();
        }


    }


    @Override
    public void handleSending(PacketEvent event) {
        return;
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

    private void handleOtherFlying(final Packet packet) {
        if(packet.getType() == PacketType.Play.Client.FLYING || packet.isPosition()) {
            deltaYaw = 0;
            deltaPitch = 0;
        }
    }


}

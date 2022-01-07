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
    private double gcdYaw, gcdPitch, absGcdPitch, absGcdYaw, sensVerbose;
    private long expandedGcdYaw, expandedGcdPitch;
    private int sensitivity, calcSensitivity, ticksSinceCinematic;
    private final Set<Integer> candidates = new HashSet<>();

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

            processSensitivity(this);
            handleCinematic();

        }

    }

    private void updateRot(final Packet packet) {
        if (packet.isPosLook()) {
            final WrapperPlayClientPositionLook wrapped = new WrapperPlayClientPositionLook(packet);
            this.yaw = wrapped.getYaw();
            this.pitch = wrapped.getPitch();
        } else if (packet.isLook()) {
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

        if (sensitivity == -1 && (exponentialPitch || exponentialYaw || invalidYaw || invalidPitch)) {
            this.ticksSinceCinematic = 0;

        }


    }


    private void handleOtherFlying(final Packet packet) {
        if (packet.getType() == PacketType.Play.Client.FLYING || packet.isPosition()) {
            deltaYaw = 0;
            deltaPitch = 0;
        }
    }

    private void processSensitivity(final RotationProcessor rotationProcessor) {

        final float pitch = rotationProcessor.getPitch();
        final float lPitch = rotationProcessor.getLastPitch();

        final float yaw = rotationProcessor.getYaw();
        final float lYaw = rotationProcessor.getLastYaw();

        if (Math.abs(pitch) != 90.0f) {
            float distanceY = pitch - lPitch;
            double error = Math.max(Math.abs(pitch), Math.abs(lPitch)) * 3.814697265625E-6;
            computeSensitivity(distanceY, error);
        }

        float distanceX = circularDistance(yaw, lYaw);
        double error = Math.max(Math.abs(yaw), Math.abs(lYaw)) * 3.814697265625E-6;
        computeSensitivity(distanceX, error);

        if (candidates.size() == 1) {
            calcSensitivity = candidates.iterator().next();
            sensitivity = 200 * calcSensitivity / 143;
        } else {
            sensitivity = -1;
            forEach(candidates::add);
        }
    }

    public void computeSensitivity(double delta, double error) {
        double start = delta - error;
        double end = delta + error;
        forEach(s -> {
            double f0 = ((double) s / 142.0) * 0.6 + 0.2;
            double f = (f0 * f0 * f0 * 8.0) * 0.15;
            int pStart = (int) Math.ceil(start / f);
            int pEnd = (int) Math.floor(end / f);
            if (pStart <= pEnd) {
                for (int p = pStart; p <= pEnd; p++) {
                    double d = p * f;
                    if (!(d >= start && d <= end)) {
                        candidates.remove(s);
                    }
                }
            } else {
                candidates.remove(s);
            }
        });
    }

    public float circularDistance(float a, float b) {
        float d = Math.abs(a % 360.0f - b % 360.0f);
        return d < 180.0f ? d : 360.0f - d;
    }

    public void forEach(Consumer<Integer> consumer) {
        for (int s = 0; s <= 143; s++) {
            consumer.accept(s);
        }
    }


}

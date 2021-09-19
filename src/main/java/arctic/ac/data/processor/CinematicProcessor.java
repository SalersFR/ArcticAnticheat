package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CinematicProcessor {

    private final PlayerData data;
    private double lastDeltaPitch,lastDeltaYaw,lastAccelPitch,lastAccelYaw;
    private int ticksSince;

    public void process(final RotationEvent event) {

        final float deltaPitch = event.getDeltaPitch();
        final double lastDeltaPitch = this.lastDeltaPitch;

        this.lastDeltaPitch = deltaPitch;

        final double deltaYaw = event.getDeltaYaw();
        final double lastDeltaYaw = this.lastDeltaYaw;

        this.lastDeltaYaw = deltaYaw;

        final double accelYaw = Math.abs(deltaYaw - lastDeltaYaw);
        final double lastAccelYaw = this.lastAccelYaw;

        this.lastAccelYaw = accelYaw;

        final double accelPitch = Math.abs(deltaPitch - lastDeltaPitch);
        final double lastAccelPitch = this.lastAccelPitch;

        this.lastAccelPitch = accelPitch;

        final double accelAccelYaw = Math.abs(accelYaw - lastAccelYaw);
        final double accelAccelPitch = Math.abs(accelPitch - lastAccelPitch);

        final double sensivity = MathUtils.getSensitivity(deltaPitch, (float) lastDeltaPitch);

        final boolean invalidYaw = accelAccelYaw< .05 && accelAccelYaw > 0;
        final boolean invalidPitch = accelAccelPitch < .05 && accelAccelPitch > 0;

        final boolean exponentialYaw = String.valueOf(accelAccelYaw).contains("E");
        final boolean exponentialPitch = String.valueOf(accelAccelPitch).contains("E");

        if(sensivity < 100 && (exponentialPitch || exponentialYaw || invalidYaw || invalidPitch)) {
            this.ticksSince = 0;

        }

        this.ticksSince++;
    }
}

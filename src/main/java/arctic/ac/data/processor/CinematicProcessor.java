package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CinematicProcessor {

    //This is the minimum rotation constant
    private static final double CINEMATIC_CONSTANT = 7.8125E-3;
    private final PlayerData data;
    private double lastDeltaPitch, lastDeltaYaw, lastAccelPitch, lastAccelYaw;
    private int ticksSince;

    public void process(final RotationEvent event) {

        final float deltaPitch = event.getDeltaPitch();
        final double lastDeltaPitch = this.lastDeltaPitch;

        this.lastDeltaPitch = deltaPitch;

        final float deltaYaw = event.getDeltaYaw();
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

        final boolean invalidYaw = accelAccelYaw < .05 && accelAccelYaw > 0;
        final boolean invalidPitch = accelAccelPitch < .05 && accelAccelPitch > 0;

        final boolean exponentialYaw = String.valueOf(accelAccelYaw).contains("E");
        final boolean exponentialPitch = String.valueOf(accelAccelPitch).contains("E");

        if (sensivity < 100 && (exponentialPitch || exponentialYaw || invalidYaw || invalidPitch)) {
            this.ticksSince = 0;

        }


        //Fixes exploits
        if (deltaPitch == 0F || deltaYaw == 0F) return;


        final boolean invalid = MathUtils.isScientificNotation(accelYaw)
                || accelPitch == 0F
                || MathUtils.isScientificNotation(accelPitch)
                || accelYaw == 0F;


        final double yawGcd = MathUtils.getAbsoluteGcd(deltaYaw, (float) lastDeltaYaw);
        final double pitchGcd = MathUtils.getAbsoluteGcd(deltaPitch, (float) lastDeltaPitch);

        final double constantYaw = yawGcd / MathUtils.EXPANDER;
        final double constantPitch = pitchGcd / MathUtils.EXPANDER;


        final boolean cinematic = !invalid && accelYaw < 1F && accelPitch < 1F;

        if (cinematic) {

            if (constantYaw < CINEMATIC_CONSTANT && constantPitch < CINEMATIC_CONSTANT) this.ticksSince = 0;

        }


        this.ticksSince++;
    }


}

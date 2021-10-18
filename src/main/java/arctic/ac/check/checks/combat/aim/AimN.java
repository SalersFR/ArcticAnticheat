package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AimN extends Check {

    private final ArcticQueue<Float> deltaYawSamples = new ArcticQueue<Float>(120);
    private double lastDeltaYaw;
    private double lastSkewness, lastDev, lastKurtosis;

    public AimN(PlayerData data) {
        super(data, "Aim", "N", "combat.aim.n", "Checks if rotations are legit using stats.",true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            final RotationEvent event = (RotationEvent) e;

            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 120L;

            final double deltaYaw = event.getDeltaYaw();

            final double lastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;

            final double accelYaw = Math.abs(deltaYaw - lastDeltaYaw);

            if (data.getCinematicProcessor().getTicksSince() < 3) return;

            if (!exemptCombat && event.getDeltaPitch() > 0.3D && accelYaw > 0.012D)
                this.deltaYawSamples.add(event.getDeltaYaw());

            try {


                final double kurtosis = MathUtils.getKurtosis(deltaYawSamples);
                final double standardDev = MathUtils.getStandardDeviation(deltaYawSamples);
                final double skewness = MathUtils.getSkewness(deltaYawSamples);


                if (deltaYawSamples.size() >= 120) {
                    debug("d=" + standardDev + "\nk=" + kurtosis + "\ns=" + skewness);


                    if (kurtosis > 7590.0D && kurtosis != lastKurtosis) {
                        if (++buffer > 2)
                            fail("kurtosis=" + kurtosis);
                    } else if (buffer > 0) buffer -= 1.25D;

                    if (Double.toString(skewness).contains("E") && deltaYaw > 4.2D && lastSkewness != skewness) {
                        if (++buffer > 5)
                            fail("skewness=" + skewness);
                    } else if (buffer > 0) buffer -= 0.25D;

                    if (standardDev > 409 && standardDev != lastDev) {
                        if (++buffer > 5)
                            fail("deviation=" + standardDev);
                    } else if (buffer > 0) buffer -= 0.25D;


                }

                this.lastKurtosis = kurtosis;
                this.lastDev = standardDev;
                this.lastSkewness = skewness;
            } catch (IndexOutOfBoundsException ex) {

            }



        }

    }
}

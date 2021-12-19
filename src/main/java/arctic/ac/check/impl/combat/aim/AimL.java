package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.packet.type.PacketType;

public class AimL extends Check {

    private float lastDeltaPitch;

    public AimL(PlayerData data) {
        super(data, "Aim", "L", "combat.aim.l", "Checks for a simple GCD bypassing method.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_LOOK ||packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor event = data.getRotationProcessor();

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final float gcd = (float) MathUtils.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

            if (gcd == 0 && ((event.getDeltaYaw() > 1.1 && event.getDeltaPitch() != 0.0f) ||
                    event.getDeltaPitch() > 0.7f) && (event.getDeltaPitch() < 15 && event.getDeltaYaw() < 15)
                    && event.getTicksSinceCinematic() > 0) {
                if (++buffer > 10) {
                    fail("GCD=" + gcd + ", dYaw=" + event.getDeltaYaw() + ", dPitch=" + deltaPitch);
                }
            } else if (buffer > 0) buffer -= 0.5;
        }
    }
}

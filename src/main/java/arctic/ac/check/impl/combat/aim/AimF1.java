package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.packet.type.PacketType;

public class AimF1 extends Check {

    public AimF1(PlayerData data) {
        super(data, "Aim", "F1", "combat.aim.f", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final float deltaPitch = rotationProcessor.getDeltaPitch();
            final float deltaYaw = rotationProcessor.getDeltaYaw();

            final float gcd = (float) rotationProcessor.getGcdPitch();

            final boolean check = (Math.abs(deltaPitch) > 0.45 || Math.abs(deltaYaw) > 0.45)
                    && rotationProcessor.getTicksSinceCinematic() <= 5 && deltaPitch < 15 && deltaYaw < 16;

            final double resYaw = deltaYaw % gcd;
            final double resPitch = deltaPitch % gcd;

            final double modulusResPitch = MathUtils.getReversedModulus(gcd, deltaPitch, resPitch);
            final double modulusResYaw = MathUtils.getReversedModulus(gcd, deltaYaw, resYaw);


            if ((Double.isNaN(modulusResPitch) || Double.isNaN(modulusResYaw)) && check) {
                if (++buffer > 13) {
                    fail("modPitch: " + modulusResPitch + ", modYaw: " + modulusResYaw +
                            ", dYaw: " + deltaYaw + ", dPitch: " + deltaPitch);
                } else if (buffer > 0) buffer -= 0.5D;
            }

            debug("modPitch: " + modulusResPitch + ", modYaw: " + modulusResYaw +
                    ", dYaw: " + deltaYaw + ", dPitch: " + deltaPitch + " b=" + buffer);
        }


    }
}

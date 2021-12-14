package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimD extends Check {

    public AimD(PlayerData data) {
        super(data, "Aim", "D", "combat.aim.d", "Checks for invalid sensitivity (gcd).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if(packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double pitch = rotationProcessor.getPitch();
            final double gcd = rotationProcessor.getExpandedGcdPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || rotationProcessor.getDeltaYaw() < 3.5D
                    || rotationProcessor.getPitchAccel() == 0.0F ||
                    rotationProcessor.getTicksSinceCinematic() <= 10 || rotationProcessor.getDeltaPitch() == 0 || rotationProcessor.getLastDeltaPitch() == 0;

            if (!exempt && rotationProcessor.getDeltaPitch() < 20) {
                if (gcd < 131072L) {
                    if (buffer < 15) buffer++;

                    if (buffer > 8)
                        fail("gcd=" + gcd);

                } else if (buffer >= 1.5D) buffer -= 1.5D;
            }


            debug("gcd=" + gcd + " buffer=" + buffer);
        }

    }
}

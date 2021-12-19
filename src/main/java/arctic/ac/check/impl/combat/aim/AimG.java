package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimG extends Check {

    public AimG(PlayerData data) {
        super(data, "Aim", "G", "combat.aim.g", "Checks for invalid & very small gcd.", true);
    }
    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double gcd = rotationProcessor.getAbsGcdPitch();

            if (gcd < 0.01 && gcd != 0 && (rotationProcessor.getDeltaPitch() < 15 && rotationProcessor.getDeltaYaw() < 15)
                    && rotationProcessor.getTicksSinceCinematic() > 3) {
                if (++buffer > 10) {
                    fail("GCD=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.5;


        }

    }
}

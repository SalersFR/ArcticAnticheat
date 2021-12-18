package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimF extends Check {

    public AimF(PlayerData data) {
        super(data, "Aim", "F", "combat.aim.f", "Checks for gcd-fix modules.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double deltaYaw = rotationProcessor.getDeltaYaw();

            final double gcd = rotationProcessor.getGcdPitch();
            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 7.2 || deltaPitch == 0.0f || rotationProcessor.getTicksSinceCinematic() <= 5;


            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + exempt + " buffer=" + buffer);

            if (gcd <= 0.0 && !exempt) {
                if (++buffer > 13) {
                    fail("gcd=" + gcd);
                }
            }  else if (buffer > 0) buffer -= 0.125D;
        }


    }
}

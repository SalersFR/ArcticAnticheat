package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimG1 extends Check {

    public AimG1(PlayerData data) {
        super(data, "Aim", "G1", "combat.aim.g", "Checks for too small gcd.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double deltaYaw = rotationProcessor.getDeltaYaw();

            final double gcd = rotationProcessor.getAbsGcdPitch();

            final boolean check =  ((deltaYaw > 1.1 && deltaPitch != 0.0f) ||
                    deltaPitch > 0.7f) && (deltaYaw < 15 && deltaPitch < 15) && rotationProcessor.getTicksSinceCinematic() > 0;


            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + !check + " buffer=" + buffer);

            if (gcd == 0.0 && check) {
                if (++buffer > 10) {
                    fail("gcd=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.25D;
        }


    }
}


package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimA extends Check {

    public AimA(PlayerData data) {
        super(data, "Aim", "A", "combat.aim.a", "Checks for a valid sensitivity.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean exempt = rotationProcessor.getDeltaYaw() < 3.2F || rotationProcessor.getDeltaYaw() > 67.25f;

            debug("sens=" + rotationProcessor.getSensitivity() + " exempt=" + exempt + " buffer=" + buffer);

            if (!exempt && rotationProcessor.getSensitivity() >= 405 && rotationProcessor.getYawAccel() <= 5 && rotationProcessor.getDeltaPitch() <= 6.75F) {
                if (++buffer > 10) {
                    fail("buffer=" + buffer);
                }
            } else if (buffer > 0) buffer -= 0.065;
        }

    }
}

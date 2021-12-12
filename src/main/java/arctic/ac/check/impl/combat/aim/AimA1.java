package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimA1 extends Check {

    private int lastSensitivity;

    public AimA1(PlayerData data) {
        super(data, "Aim", "A1", "combat.aim.a", "Checks for a valid sensitivity.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean exempt = rotationProcessor.getDeltaYaw() < 3.2F || rotationProcessor.getDeltaYaw() > 67.25f;
            final int sensitivity = rotationProcessor.getSensitivity();

            debug("sens=" + rotationProcessor.getSensitivity() + " exempt=" + exempt + " buffer=" + buffer);

            if (!exempt && sensitivity >= 125 && rotationProcessor.getYawAccel() <= 5 &&
                    rotationProcessor.getDeltaPitch() <= 6.75F && sensitivity == lastSensitivity) {
                if (++buffer > 16) {
                    fail("buffer=" + buffer);
                }
            } else if (buffer > 0) buffer -= 0.2;

            this.lastSensitivity = sensitivity;
        }

    }
}

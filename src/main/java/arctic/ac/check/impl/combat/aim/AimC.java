package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimC extends Check {

    public AimC(PlayerData data) {
        super(data, "Aim", "C", "combat.aim.c", "Checks for smooth rotations (yaw).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double accelYaw = rotationProcessor.getYawAccel();

            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || rotationProcessor.getDeltaYaw() < 3.5D;

            if(accelYaw <= 0.0075 && !exempt) {
                if(++buffer > 4)
                    fail("diff=" + accelYaw);
            } else if(buffer > 0) buffer -= 0.1025;

            debug("acc=" + accelYaw + " buffer=" + buffer);
        }

    }
}

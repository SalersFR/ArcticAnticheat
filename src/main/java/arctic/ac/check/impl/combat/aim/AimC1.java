package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimC1 extends Check {

    public AimC1(PlayerData data) {
        super(data, "Aim", "C1", "combat.aim.c", "Checks for smooth rotations (pitch).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double accelPitch = rotationProcessor.getPitchAccel();

            final double pitch = rotationProcessor.getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || rotationProcessor.getDeltaYaw() < 9.5D;

            if(accelPitch <= 0.001 && !exempt) {
                if(++buffer > 7)
                    fail("diff=" + accelPitch);
            } else if(buffer > 0) buffer -= 0.1025;

            debug("acc=" + accelPitch + " buffer=" + buffer);
        }

    }
}
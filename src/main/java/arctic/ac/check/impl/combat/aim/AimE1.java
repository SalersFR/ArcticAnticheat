package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimE1 extends Check {

    public AimE1(PlayerData data) {
        super(data, "Aim", "E1", "combat.aim.e", "Checks for rounded rots values (pitch).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();

            if(deltaPitch > 3.25D && deltaPitch % 1 == 0) {
                if(++buffer > 1)
                    fail("deltaYaw=" + deltaPitch);
            } else if(buffer > 0) buffer -= 0.02;

        }
    }
}

package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimE extends Check {

    public AimE(PlayerData data) {
        super(data, "Aim", "E", "combat.aim.e", "Checks for rounded rots values (yaw).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();

            if(deltaYaw > 3.25D && deltaYaw % 1 == 0) {
                if(++buffer > 1)
                    fail("deltaYaw=" + deltaYaw);
            } else if(buffer > 0) buffer -= 0.02;

        }
    }
}

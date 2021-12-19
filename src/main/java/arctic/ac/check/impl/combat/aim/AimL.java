package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;

public class AimL extends Check {

    public AimL(PlayerData data) {
        super(data, "Aim", "L", "combat.aim.l", "Checks impossible small rots (yaw).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            if(Double.toString(data.getRotationProcessor().getDeltaYaw()).contains("E")) {
                if(++buffer > 1)
                    fail("delta="+ data.getRotationProcessor().getDeltaYaw());
            } else if(buffer > 0) buffer -= 0.0025D;
        }
    }
}

package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;

public class AimL1 extends Check {

    public AimL1(PlayerData data) {
        super(data, "Aim", "L1", "combat.aim.l", "Checks impossible small rots (pitch).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            if (Double.toString(data.getRotationProcessor().getDeltaPitch()).contains("E") && data.getRotationProcessor().getTicksSinceCinematic() > 2) {
                if (++buffer > 1)
                    fail("delta=" + data.getRotationProcessor().getDeltaPitch());
            } else if (buffer > 0) buffer -= 0.0025D;
        }
    }
}

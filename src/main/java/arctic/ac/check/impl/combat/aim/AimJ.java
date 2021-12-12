package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimJ extends Check {

    public AimJ(PlayerData data) {
        super(data, "Aim", "J", "combat.aim.j", "Checks for same rotation (yaw).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if(packetType == PacketType.IN_LOOK ||packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final double lastDeltaYaw = rotationProcessor.getLastDeltaYaw();

            final double deltaPitch = rotationProcessor.getDeltaPitch();

            if(deltaPitch > 0.4 && deltaYaw == lastDeltaYaw) {
                if(++buffer > 8) {
                    buffer /= 2.5D;
                    fail("delta=" + deltaYaw + " last=" + lastDeltaYaw);
                }
            } else if(buffer > 0) buffer--;
        }

    }
}

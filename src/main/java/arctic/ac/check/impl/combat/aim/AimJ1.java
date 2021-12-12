package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimJ1 extends Check {

    public AimJ1(PlayerData data) {
        super(data, "Aim", "J1", "combat.aim.j", "Checks for same rotation (pitch).", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if(packetType == PacketType.IN_LOOK ||packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();
            
            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double lastDeltaPitch = rotationProcessor.getLastDeltaPitch();

            if(deltaPitch > 0.4 && deltaPitch == lastDeltaPitch) {
                if(++buffer > 8) {
                    buffer /= 2.5D;
                    fail("delta=" + deltaPitch + " last=" + lastDeltaPitch);
                }
            } else if(buffer > 0) buffer--;
        }

    }
}

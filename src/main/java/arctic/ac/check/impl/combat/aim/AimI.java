package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;

public class AimI extends Check {

    private long lastRotated;

    public AimI(PlayerData data) {
        super(data, "Aim", "I", "combat.aim.i", "Checks for robot-like rotations.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {

            final long now = System.currentTimeMillis();
            final long before = lastRotated;

            this.lastRotated = now;

            final double delta = data.getRotationProcessor().getDeltaYaw();
            final long diff = now - before;

            debug("diff=" + diff + " delta=" + delta + " buffer=" + buffer);
            if (diff > 250 && diff < 400) {
                if (delta > 3) {
                    buffer++;
                    if (buffer > 3.75) {
                        fail("&9time&f: " + diff + " &9buffer&f: " + buffer);
                    }
                }
            } else if (buffer > 0) buffer -= 0.025;
        }

    }
}

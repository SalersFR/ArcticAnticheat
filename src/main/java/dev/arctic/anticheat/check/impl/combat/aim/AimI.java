package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class AimI extends Check {

    private long lastRotated;

    public AimI(PlayerData data) {
        super(data, "Aim", "I", "combat.aim.i", "Checks for robot-like rotations.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isRotation()) {

            final long before = lastRotated;
            this.lastRotated = time;

            final double delta = data.getRotationProcessor().getDeltaYaw();
            final long diff = time - before;

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

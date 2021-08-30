package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;


public class BadPacketsB extends Check {


    public BadPacketsB(PlayerData data) {
        super(data, "BadPackets", "B", "player.badpackets.b", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final float pitch = event.getTo().getPitch();

            if (pitch > 90.0f) {
                fail("pitch=" + pitch);
            }
        }
    }

}


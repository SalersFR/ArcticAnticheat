package polar.ac.check.checks.player.badpackets;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.RotationEvent;


public class BadPacketsB extends Check {


    public BadPacketsB(PlayerData data) {
        super(data, "BadPackets", "B", "player.badpackets.b", false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final float pitch = event.getTo().getPitch();

            if(pitch > 90.0f) {
                fail("pitch=" + pitch);
            }
        }
    }

}


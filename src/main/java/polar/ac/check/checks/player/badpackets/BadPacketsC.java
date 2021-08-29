package polar.ac.check.checks.player.badpackets;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.RotationEvent;
import polar.ac.event.client.UseEntityEvent;

public class BadPacketsC extends Check {


    public BadPacketsC(PlayerData data) {
        super(data, "BadPackets", "C", "player.badpackets.c", false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if(event.getTarget().getUniqueId() == data.getPlayer().getUniqueId()) {
                fail("attacked himself");
            }

        }
    }
}

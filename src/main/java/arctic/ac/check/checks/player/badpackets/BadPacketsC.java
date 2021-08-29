package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;

public class BadPacketsC extends Check {


    public BadPacketsC(PlayerData data) {
        super(data, "BadPackets", "C", "player.badpackets.c", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getTarget().getUniqueId() == data.getPlayer().getUniqueId()) {
                fail("attacked himself");
            }

        }
    }
}

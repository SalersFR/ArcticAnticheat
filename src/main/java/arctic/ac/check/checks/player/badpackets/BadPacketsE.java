package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.MoveEvent;

public class BadPacketsE extends Check {

    private int ticks;

    public BadPacketsE(PlayerData data) {
        super(data, "BadPackets", "E", "player.badpackets.e", "Checks for invalid pos packets.", false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof FlyingEvent) {
            if(++this.ticks >= 21) {
                if(++buffer > 0)
                    fail("ticks=" + ticks);
            } else if(buffer > 0) buffer -= 0.0025D;
        } else if(e instanceof MoveEvent) {
            this.ticks = 0;
        }
    }
}

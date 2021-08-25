package polar.ac.check.checks.combat.killaura;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.event.client.UseEntityEvent;

public class KillAuraH extends Check {

    public KillAuraH(PlayerData data) {
        super(data, "KillAura", "H", "combat.killaura.h", true);
    }

    /*
    One of Funke's checks.
     */

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            data.getPosData().setLastFlying(System.currentTimeMillis());
        } else if (e instanceof UseEntityEvent) {
            if (System.currentTimeMillis() - data.getPosData().getLastFlying() < 5) {
                if (++buffer > 5.0) {
                    fail("lastFlying=" + (System.currentTimeMillis() - data.getPosData().getLastFlying()));
                }
            } else if (buffer > 0) buffer -= 0.25;
        }
    }
}

package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class KillAuraC extends Check {

    private int ticks, lastID;

    public KillAuraC(PlayerData data) {
        super(data, "KillAura", "C", "combat.killaura.c", "Checks if player is attacking two entities at once.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {
            this.ticks = 0;

        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;


            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                final int id = event.getTarget().getEntityId();

                debug("id=" + id + " lastID=" + lastID + " ticks=" + ticks);

                if (id != lastID) {
                    if (++ticks > 1) {
                        fail("ticks=" + ticks);
                    }
                }

                this.lastID = id;


            }
        }
    }


}

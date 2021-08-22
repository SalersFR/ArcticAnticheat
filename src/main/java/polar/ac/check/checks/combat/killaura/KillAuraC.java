package polar.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.FlyingEvent;
import polar.ac.event.client.UseEntityEvent;

public class KillAuraC extends Check {

    private int ticks, lastID;

    public KillAuraC(PlayerData data) {
        super(data, "KillAura", "C", "combat.killaura.c", true);
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

package polar.ac.check.checks.combat.killaura;

import com.comphenix.protocol.wrappers.EnumWrappers;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.FlyingEvent;
import polar.ac.event.client.UseEntityEvent;

public class KillAuraA extends Check {

    private long lastFlying;

    public KillAuraA(PlayerData data) {
        super(data, "KillAura", "A", "combat.killaura.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {

            this.lastFlying = ((FlyingEvent) e).getTime();

        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {


                final long elapsed = Math.abs(System.currentTimeMillis() - this.lastFlying);

                debug("elapsed=" + elapsed + " current=" + System.currentTimeMillis() + " last=" + lastFlying);

                if (elapsed < 25L) {
                    if (++this.buffer > 7) {
                        fail("elapsed=" + elapsed);
                    }
                } else if (this.buffer > 0) this.buffer--;


            }
        }

    }
}

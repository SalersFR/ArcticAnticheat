package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class KillAuraA extends Check {

    private long lastFlying;

    public KillAuraA(PlayerData data) {
        super(data, "KillAura", "A", "combat.killaura.a", "Checks for invalid delay between attack and flying packets.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {

            this.lastFlying = ((FlyingEvent) e).getTime();

        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;

            if (event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {


                final long elapsed = Math.abs(System.currentTimeMillis() - this.lastFlying);

                final int ping = ((CraftPlayer) data.getPlayer()).getHandle().ping;

                if (ping > 125) return;

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

package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class KillAuraA extends Check {

    private long lastFlying, lastFlyingDelay;
    private double average = 50;
    private int hits;

    public KillAuraA(PlayerData data) {
        super(data, "KillAura", "A", "combat.killaura.a", "Checks for invalid delay between attack and flying packets.", true);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof FlyingEvent) {
            this.lastFlyingDelay = System.currentTimeMillis() - lastFlying;
            this.lastFlying = ((FlyingEvent) e).getTime();
        } else if(e instanceof UseEntityEvent) {
            final UseEntityEvent event = (UseEntityEvent) e;

            if(event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
                final long delta = Math.abs(System.currentTimeMillis() - this.lastFlying);

                average = ((average * 14) + delta) / 15;

                debug("elapsed=" + delta + " current=" + System.currentTimeMillis() + " last=" + lastFlying);

                if(lastFlyingDelay > 10L && lastFlyingDelay < 90L) {
                    if(average < 5 && hits++ > 10) {
                        fail("delta="+average);
                        average = 5;
                    }
                }
            }
        }

    }
}

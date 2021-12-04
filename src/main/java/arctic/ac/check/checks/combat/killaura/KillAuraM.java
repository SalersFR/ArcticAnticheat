package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

import java.util.Locale;

public class KillAuraM extends Check {

    private long lastBlock;
    private int avgDiff, avgDev;


    public KillAuraM(final PlayerData data) {
        super(data, "KillAura", "M", "combat.killaura.m", "Checks for auto-block modules (interact).", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            final UseEntityEvent event = (UseEntityEvent) e;
            if (event.getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT
                    && data.getPlayer().getItemInHand().getType().toString().toLowerCase(Locale.ROOT).contains("sword")) {

                final int diff = (int) getMillis(lastBlock);
                avgDiff = ((avgDiff * 14) + diff) / 15;

                final int deviation = Math.abs(diff - avgDiff);
                avgDev = ((avgDev * 14) + deviation) / 15;

                debug("avgDev=" + avgDev + " avgDiff=" + avgDiff);

                if(avgDev <= 5) {
                    if(++buffer > 10)
                        fail("avgDev=" + avgDev);
                } else if(buffer > 0) buffer -= 0.25D;


                this.lastBlock = System.currentTimeMillis();

            }



        }
    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}

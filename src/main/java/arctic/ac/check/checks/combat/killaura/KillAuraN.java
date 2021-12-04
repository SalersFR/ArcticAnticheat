package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.PacketReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

import java.util.Locale;

public class KillAuraN extends Check {

    private long lastBlock;
    private int avgDiff, avgDev, lastAvgDev;


    public KillAuraN(final PlayerData data) {
        super(data, "KillAura", "N", "combat.killaura.n", "Checks for auto-block modules (interact-at).", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketReceiveEvent) {
            final PacketReceiveEvent event = (PacketReceiveEvent) e;

            if (event.getPacketType() == PacketType.Play.Client.BLOCK_DIG &&
                    data.getPlayer().getItemInHand().getType().toString().toLowerCase(Locale.ROOT).contains("sword")
                    && getMillis(data.getInteractData().getLastHitPacket()) < 100L) {

                final int diff = (int) getMillis(lastBlock);
                avgDiff = ((avgDiff * 14) + diff) / 15;

                final int deviation = Math.abs(diff - avgDiff);
                avgDev = ((avgDev * 14) + deviation) / 15;

                debug("avgDev=" + avgDev + " avgDiff=" + avgDiff);

                if (avgDev <= 21 && (avgDev == lastAvgDev || avgDiff < lastAvgDev) && avgDiff < 250) {
                    if (++buffer > 12)
                        fail("avgDev=" + avgDev + " avgDiff=" + avgDiff);
                } else if (buffer > 0) buffer -= (10 / 3);


                this.lastBlock = System.currentTimeMillis();
                this.lastAvgDev = avgDev;

            }


        }
    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}

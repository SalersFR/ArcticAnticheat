package arctic.ac.check.checks.player.scaffold;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.BlockPlaceEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class ScaffoldA extends Check {

    private final ArcticQueue<Integer> samples = new ArcticQueue<>(30);
    private long lastTime;

    public ScaffoldA(PlayerData data) {
        super(data, "Scaffold", "A", "player.scaffold.a", "Checks for proper delay between block places.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof BlockPlaceEvent) {

            final BlockPlaceEvent event = (BlockPlaceEvent) e;

            final long time = event.getTime();
            final long lastTime = this.lastTime;

            this.lastTime = time;

            final int delta = (int) (time - lastTime);

            final double average = MathUtils.averageInt(samples);
            final double deviation = MathUtils.getStandardDeviation(samples);

            if (samples.size() != 30) return;

            if (deviation > 0.0D && deviation < 4.25D && average < 60D) {
                if (++buffer > 2)
                    fail("dev=" + deviation + " delta=" + delta);
            } else if (buffer > 0) buffer -= 0.1D;

            debug("dev=" + deviation + " avg=" + average);


        }

    }
}

package dev.arctic.anticheat.check.impl.player.scaffold;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

import java.util.ArrayList;

/**
 * @author xWand
 */
public class ScaffoldB extends Check {

    private final ArrayList<Long> samples = new ArrayList<>(10);

    private long lastTime, lastDelta;

    public ScaffoldB(PlayerData data) {
        super(data, "Scaffold", "B", "player.scaffold.b", "Checks for placing too fast.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isBlockPlace() || packet.isAbilities()) {
            final long lastTime = this.lastTime;
            this.lastTime = time;

            final long delta = time - lastTime;
            final long lastDelta = this.lastDelta;
            this.lastTime = delta;

            if (delta > 5L && delta != lastDelta) this.samples.add(delta);

            if (!(this.samples.size() > 10)) return;

            final double deviation = MathUtils.getStandardDeviation(this.samples);
            final double average = MathUtils.averageLong(this.samples);

            if (deviation > 0D && deviation < 4.25D && average < 65D) {
                fail();
            }
        }
    }
}

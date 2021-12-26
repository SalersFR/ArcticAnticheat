package dev.arctic.anticheat.check.impl.combat.autoclicker;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerC extends Check {

    public AutoclickerC(PlayerData data) {
        super(data, "Autoclicker", "C", "combat.autoclicker.c", "Checks for impossible consistency.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final double deviation = clickProcessor.getDeviation();
            final int outliers = clickProcessor.getOutliers();
            final int sames = clickProcessor.getSames();

            debug("dev=" + deviation + " outliers=" + outliers + " sames=" + sames);

            if ((deviation < 1.525D || outliers <= 3) && sames > 17) {
                buffer += (1.5 - deviation);
                if (buffer > 4.25)
                    fail("buffer=" + buffer + " dev=" + deviation);
            } else if (buffer > 0) buffer -= 0.125D;
        }

    }
}

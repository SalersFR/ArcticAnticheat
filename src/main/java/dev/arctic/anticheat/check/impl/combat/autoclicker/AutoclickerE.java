package dev.arctic.anticheat.check.impl.combat.autoclicker;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerE extends Check {


    private int lastOutliers;

    public AutoclickerE(PlayerData data) {
        super(data, "Autoclicker", "E", "combat.autoclicker.e", "Checks for low outliers.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final int outliers = clickProcessor.getOutliers();
            final int outliersDiff = Math.abs(outliers - lastOutliers);

            debug("diff=" + outliersDiff + " buffer=" + buffer);

            if(outliersDiff <= 1) {
                buffer += (2 - outliersDiff);
                if(buffer > 7.25)
                    fail("diff=" + outliersDiff);
            } else if(buffer > 0) buffer -= 0.2D;

            this.lastOutliers = outliers;


        }

    }
}

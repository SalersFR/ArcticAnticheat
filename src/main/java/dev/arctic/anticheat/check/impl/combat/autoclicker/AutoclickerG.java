package dev.arctic.anticheat.check.impl.combat.autoclicker;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerG extends Check {

    private double lastEntropy = -1;
    private int lastOutliers = -1;

    public AutoclickerG(PlayerData data) {
        super(data, "Autoclicker","G", "combat.autoclicker.g", "Checks for clicker flaws.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if(clickProcessor.isNotAbleToCheck()) return;

            final double entropy = clickProcessor.getEntropy();
            final int outliers = clickProcessor.getOutliers();
            final double cps = clickProcessor.getCps();

            final double entropyDiff = Math.abs(entropy % lastEntropy);
            final int outliersDiff = Math.abs(outliers - lastOutliers);

            if(entropyDiff <= 0.2 && cps >= 10.2525 && (outliersDiff <= 1 || outliers <= 1) && outliers <= 10) {
                if(++buffer > 4)
                    fail("ent=" + entropy + " outls=" + outliers);
            } else if(buffer > 0) buffer -= 0.02D;


            this.lastEntropy = entropy;
            this.lastOutliers = outliers;

            debug("outliers=" + outliers + " cps=" + cps + " entropy=" + entropy);
        }


    }
}

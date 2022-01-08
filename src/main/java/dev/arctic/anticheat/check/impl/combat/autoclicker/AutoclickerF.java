package dev.arctic.anticheat.check.impl.combat.autoclicker;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerF extends Check {

    public AutoclickerF(PlayerData data) {
        super(data, "Autoclicker", "F", "combat.autoclicker.f", "Checks for rounded stats values.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final double kurtosis = clickProcessor.getKurtosis();
            final double skewness = clickProcessor.getSkewness();
            final double deviation = clickProcessor.getDeviation();
            final double variance = clickProcessor.getVariance();
            final double entropy = clickProcessor.getEntropy();
            final double cps = clickProcessor.getCps();

            if (isRound(kurtosis)) {
                if (++buffer > 3)
                    fail("kurt=" + kurtosis);
            } else if (buffer > 0) buffer -= 0.01D;

            if (isRound(skewness)) {
                if (++buffer > 3)
                    fail("skew=" + skewness);
            } else if (buffer > 0) buffer -= 0.01D;

            if (isRound(deviation)) {
                if (++buffer > 3)
                    fail("std=" + deviation);
            } else if (buffer > 0) buffer -= 0.01D;

            if (isRound(variance)) {
                if (++buffer > 4)
                    fail("var=" + variance);
            } else if (buffer > 0) buffer -= 0.2D;

            if (isRound(cps) && cps > 10) {
                if (++buffer > 3)
                    fail("cps=" + cps);
            } else if (buffer > 0) buffer -= 0.25D;

            if (isRound(entropy)) {
                if (++buffer > 3)
                    fail("ent=" + entropy);
            } else if (buffer > 0) buffer -= 0.01D;


            debug("kurt=" + kurtosis + "\nskew=" + skewness + "\nvar=" + variance + "\ndev=" + deviation);
        }
    }

    private boolean isRound(final double val) {
        return val % 0.5 == 0 || val % 1 == 0 || val % 1.5 == 0;
    }
}

package dev.arctic.anticheat.check.impl.combat.autoclicker;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

public class AutoclickerD extends Check {

    private double lastKurtosis;

    public AutoclickerD(PlayerData data) {
        super(data, "Autoclicker", "D", "combat.autoclicker.d", "Checks for low kurtosis.", true);
    }

    @Override
    public void handle(Packet packet,long time) {
        if (packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final double kurtosis = clickProcessor.getKurtosis();
            final double difference = MathUtils.hypot(kurtosis, lastKurtosis);

            debug("diff=" + difference + " buffer=" + buffer);

            if (Math.abs(difference) <= 0.55D) {
                if (++buffer > 3) {
                    fail("absDiff=" + Math.abs(difference));
                }
            } else if (buffer > 0) buffer -= 0.25;

            this.lastKurtosis = kurtosis;


        }

    }
}

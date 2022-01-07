package dev.arctic.anticheat.check.impl.combat.autoclicker;



import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerA1 extends Check {

    private double lastDeviation;

    public AutoclickerA1(PlayerData data) {
        super(data, "Autoclicker", "A1", "combat.autoclicker.a1", "Checks for poor clicker randomization.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final double deviation = clickProcessor.getDeviation();

            debug("deviation=" + deviation + " buffer=" + buffer);

            if (deviation <= 2.25D && Math.abs(deviation - lastDeviation) <= 0.075) {
                buffer += (1.5 - deviation);
                if (buffer > 4.25)
                    fail("buffer=" + buffer + " dev=" + deviation);
            } else if (buffer > 0) buffer -= 0.125D;

            this.lastDeviation = deviation;
        }

    }
}

package dev.arctic.anticheat.check.impl.combat.autoclicker;



import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.ClickProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AutoclickerA1 extends Check {

    public AutoclickerA1(PlayerData data) {
        super(data, "Autoclicker", "A1", "combat.autoclicker.a", "Checks for poor clicker randomization.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isArmAnimation()) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (clickProcessor.isNotAbleToCheck()) return;

            final double deviation = clickProcessor.getDeviation();

            debug("deviation=" + deviation + " buffer=" + buffer);

            if (deviation <= 1.2) {
                buffer += (1.5 - deviation);
                if (buffer > 4.25)
                    fail("buffer=" + buffer + " dev=" + deviation);
            } else if (buffer > 0) buffer -= 0.125D;
        }

    }
}

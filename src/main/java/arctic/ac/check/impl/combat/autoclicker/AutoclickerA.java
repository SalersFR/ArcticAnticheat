package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerA extends Check {

    public AutoclickerA(PlayerData data) {
        super(data, "Autoclicker", "A", "combat.autoclicker.a", "Checks for poor clicker randomization.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_ARM_ANIMATION) {
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

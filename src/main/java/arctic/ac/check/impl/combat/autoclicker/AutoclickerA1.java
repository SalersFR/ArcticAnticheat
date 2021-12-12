package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerA1 extends Check {

    private double lastDeviation;

    public AutoclickerA1(PlayerData data) {
        super(data, "Autoclicker", "A1", "combat.autoclicker.a", "Checks for poor clicker randomization.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_ARM_ANIMATION) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (!clickProcessor.isAbleToCheck()) return;

            final double deviation = clickProcessor.getDeviation();

            debug("deviation=" + deviation + " buffer=" + buffer);

            if (deviation <= 2.5 && Math.abs(deviation - lastDeviation) < 0.2D) {
                buffer += (1.5 - deviation);
                if (buffer > 4.25)
                    fail("buffer=" + buffer + " dev=" + deviation);
            } else if (buffer > 0) buffer -= 0.125D;

            this.lastDeviation = deviation;
        }



    }
}

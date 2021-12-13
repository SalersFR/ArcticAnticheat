package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerC extends Check {

    public AutoclickerC(PlayerData data) {
        super(data, "Autoclicker", "C", "combat.autoclickerca", "Checks for poor clicker randomization.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_ARM_ANIMATION) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (!clickProcessor.isAbleToCheck()) return;

            final double deviation = clickProcessor.getDeviation();
            final int outliers = clickProcessor.getOutliers();
            final int sames = clickProcessor.getSames();

            debug("dev=" + deviation + " outliers=" + outliers + " sames=" + sames);

            if ((deviation < 1.525D || outliers <= 3) && sames > 22) {
                buffer += (1.5 - deviation);
                if (buffer > 4.25)
                    fail("buffer=" + buffer + " dev=" + deviation);
            } else if (buffer > 0) buffer -= 0.125D;
        }

    }
}

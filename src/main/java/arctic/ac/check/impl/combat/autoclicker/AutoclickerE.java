package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerE extends Check {

    private int lastOutliers;

    public AutoclickerE(PlayerData data) {
        super(data, "Autoclicker", "E", "combat.autoclicker.e", "Checks for low outliers.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_ARM_ANIMATION) {
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

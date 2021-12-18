package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerB extends Check {

    private double lastSkewness, lastSkewnessDiff;
    private int lastOutliers;

    public AutoclickerB(PlayerData data) {
        super(data, "Autoclicker", "B", "combat.autoclicker.b", "Checks for strange values.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_ARM_ANIMATION) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (!clickProcessor.isAbleToCheck()) return;

            final double skewness = clickProcessor.getSkewness();
            final int outliers = clickProcessor.getOutliers();

            final int outlierDifference = Math.abs(outliers - lastOutliers);
            final double skewnessDifference = Math.abs(skewness - lastSkewness);

            debug("outliersDiff=" + outlierDifference + " skewnessDiff=" + skewnessDifference);

            if ((skewnessDifference < 1.0E-5 || (Double.isNaN(skewnessDifference) && Double.isNaN(lastSkewnessDiff)))
                    && outlierDifference <= 1 && (skewness < 3.5D || Double.isNaN(skewness))) {
                if (++buffer > 5)
                    fail("outliersDiff=" + outlierDifference + " skewness=" + skewness + " skewDiff=" + skewnessDifference);

            } else if (buffer > 0) buffer -= 0.25D;


            this.lastSkewness = skewness;
            this.lastOutliers = outliers;
            this.lastSkewnessDiff = skewnessDifference;


        }

    }
}

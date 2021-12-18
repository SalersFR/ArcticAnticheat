package arctic.ac.check.impl.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.ClickProcessor;
import arctic.ac.utils.MathUtils;
import eu.salers.salty.packet.type.PacketType;

public class AutoclickerD extends Check {

    private double lastKurtosis;

    public AutoclickerD(PlayerData data) {
        super(data, "Autoclicker", "D", "combat.autoclicker.d", "Checks for low kurtosis.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_ARM_ANIMATION) {
            final ClickProcessor clickProcessor = data.getClickProcessor();
            if (!clickProcessor.isAbleToCheck()) return;

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

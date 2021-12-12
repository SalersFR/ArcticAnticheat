package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;
import arctic.ac.utils.Pair;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;

import java.util.List;

public class ClickProcessor extends Processor {

    private int ticks, outliers, sames;
    private boolean ableToCheck;
    private double kurtosis, deviation, variance, skewness;
    private final ArcticQueue<Integer> samples = new ArcticQueue<>(25);

    public ClickProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if (event.getPacketType() == PacketType.IN_ARM_ANIMATION) {


            if (samples.size() >= 2) {
                final Pair<List<Double>, List<Double>> pairOutliers = MathUtils.getOutliers(this.samples);
                this.outliers = pairOutliers.getX().size() + pairOutliers.getY().size();

                this.kurtosis = MathUtils.getKurtosis(samples);
                this.deviation = MathUtils.getStandardDeviation(samples);
                this.variance = MathUtils.getVariance(samples);
                this.skewness = MathUtils.getSkewness(samples);

                this.sames = MathUtils.getSames(samples);
            }

            this.ableToCheck = this.samples.size() == 25;

            this.samples.add(ticks);

            ticks = 0;

        } else if (event.getPacketType() == PacketType.IN_FLYING || event.getPacketType() == PacketType.IN_POSITION
                || event.getPacketType() == PacketType.IN_POSITION_LOOK || event.getPacketType() == PacketType.IN_LOOK) {
            ticks++;


        } else if(event.getPacketType() == PacketType.IN_BLOCK_DIG) {
            this.samples.removeFirst();
        }

    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {

    }
}

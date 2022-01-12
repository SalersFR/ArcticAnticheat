package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.protocol.wrappers.Pair;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.EvictingList;
import dev.arctic.anticheat.utilities.MathUtils;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;

@Getter
public class ClickProcessor extends Processor {

    private int ticks, outliers, sames, placeTicks;
    private double kurtosis, deviation, variance, skewness, cps, entropy, diggingBuffer;
    private final EvictingList<Integer> samples = new EvictingList<>(20, true);

    public ClickProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isArmAnimation()) {
            if (samples.size() >= 2) {
                final Pair<List<Double>, List<Double>> pairOutliers = MathUtils.getOutliers(this.samples);
                this.outliers = pairOutliers.getFirst().size() + pairOutliers.getSecond().size();

                this.kurtosis = MathUtils.getKurtosis(samples);
                this.deviation = MathUtils.getStandardDeviation(samples);
                this.variance = MathUtils.getVariance(samples);
                this.skewness = MathUtils.getSkewness(samples);
                this.cps = MathUtils.getCps(samples);
                this.entropy = MathUtils.getEntropy(samples);

                this.sames = MathUtils.getSames(samples);



                //another dig handling
                if(cps >= 20.0 && cps % 10.0 == 0) {
                    if(++diggingBuffer > 10) {
                        samples.clear();
                        diggingBuffer = 2;
                    }

                } else if(diggingBuffer > 0) diggingBuffer -= 0.5D;
            }


            this.samples.add(ticks);

            ticks = 0;


        } else if(event.getPacket().isFlying()) {
            ticks++;
            placeTicks++;

        } else if(event.getPacket().isBlockDig()) {
            if(cps >= 15) {
                if(++diggingBuffer > 10) {
                    samples.clear();
                    diggingBuffer = 2;
                }
            }
        } else if(event.getPacket().isBlockPlace()) {
            placeTicks = 0;
        }
    }

    @Override
    public void handleSending(PacketEvent event) {


    }

    public boolean isNotAbleToCheck() {
        return samples.size() != 20;
    }



}

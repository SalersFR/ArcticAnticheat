package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;

@Getter
public class VelocityProcessor extends Processor {

    private double originalY, originalX, originalZ, velocityY;
    private int velTicks;

    public VelocityProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {
            if (velTicks > 0) {
                if (velocityY > 0) {
                    velocityY -= 0.08D;
                    velocityY *= 0.98F;

                } else {
                    velocityY = 0;
                }

                if (velocityY < 0.005) {
                    velocityY = 0;
                }

                velTicks++;
            }
        }
    }

    @Override
    public void handleSending(PacketEvent event) {
        if(event.getPacket().isVelocity()) {
            final WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(event.getPacket());

            if(packet.getEntityID() == data.getPlayer().getEntityId()) {
                final double x = packet.getVelocityX();
                final double y = packet.getVelocityY();
                final double z = packet.getVelocityZ();

                data.getTransactionProcessor().todoTransaction(() -> {
                    originalY = y;
                    velocityY = originalY;
                    velTicks = 0;
                });

                velTicks = 0;
            }

        }

    }
}

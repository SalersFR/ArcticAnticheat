package dev.arctic.anticheat.data.processors.impl;

import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class CancelProcessor extends Processor {

    private Location lastGroundLocation;

    private boolean movementCancel, hitCancel;
    private int cancelTicks;

    public CancelProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            cancelTicks++;

            if (collisionProcessor.isClientOnGround() || collisionProcessor.isMathOnGround() || collisionProcessor.isCollisionOnGround()) {
                lastGroundLocation = event.getPlayer().getLocation();
            }

            if (movementCancel && cancelTicks >= 8) {
                data.getPlayer().teleport(lastGroundLocation);
                movementCancel = false;
                cancelTicks = 0;
            }




        }
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

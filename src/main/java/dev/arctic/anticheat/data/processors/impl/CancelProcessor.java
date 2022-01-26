package dev.arctic.anticheat.data.processors.impl;

import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.BlockUtil;
import dev.arctic.anticheat.utilities.ServerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

@Getter
@Setter
public class CancelProcessor extends Processor {

    private Location lastGroundLocation;

    private boolean movementCancel, hitCancel;
    private int cancelTicks;
    private long setBackTicks;

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

    public void dragDown() {
        final long current = System.nanoTime() / 1000_000L;
        if ((current - getSetBackTicks()) > 40) {
            double ytoAdd = data.getVelocityProcessor().getVelocityY();
            if (ytoAdd > 0) {
                return;
            }
            final Location block = data.getPlayer().getLocation().clone().add(0, ytoAdd, 0);
            for (int i = 0; i < 10; i++) {
                Block asyncBlock = ServerUtil.getBlock(block);
                if(asyncBlock == null) continue;
                if (asyncBlock.getType().isSolid()) {
                    block.add(0, 0.1, 0);
                } else {
                    break;
                }
            }
            Bukkit.getScheduler().runTask(Arctic.getInstance().getPlugin(), () -> data.getPlayer().teleport(block));
        }
        setBackTicks = current;
        setBackTicks++;
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

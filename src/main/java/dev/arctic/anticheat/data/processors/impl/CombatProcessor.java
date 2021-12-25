package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

@Getter
public class CombatProcessor extends Processor {

    private Entity target, lastTarget;
    private int hitTicks;

    public CombatProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isAttack()) {
            final WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
            lastTarget = target;
            this.target = packet.getTarget(data.getPlayer().getWorld());
            this.hitTicks = 0;
        } else if(event.getPacket().isFlying()) {
            hitTicks++;
        }
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

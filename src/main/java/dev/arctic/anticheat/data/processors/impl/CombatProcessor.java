package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

@Getter
public class CombatProcessor extends Processor {

    private LivingEntity target, lastTarget;
    private int hitTicks;

    public CombatProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isAttack()) {
            final WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
            lastTarget = target;
            this.target = (LivingEntity) packet.getTarget(data.getPlayer().getWorld());
            this.hitTicks = 0;
            if (packet.getType().equals(EnumWrappers.EntityUseAction.ATTACK)) {
                data.getMovementProcessor().setSlowDown(true);
                data.getMovementProcessor().setLastAttackSlowDown(System.currentTimeMillis());
            }
        } else if(event.getPacket().isFlying()) {
            hitTicks++;
        }
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

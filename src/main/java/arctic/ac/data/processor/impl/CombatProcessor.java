package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInUseEntity;
import lombok.Getter;
import org.bukkit.entity.Entity;

@Getter
public class CombatProcessor extends Processor {

    private Entity target, lastTarget;
    private long lastUseEntity, hitTicks;

    public CombatProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {
        if(event.getPacketType() == PacketType.IN_USE_ENTITY) {
            final WrappedInUseEntity wrapped = new WrappedInUseEntity(event.getPacket());
            if(wrapped.getUseAction() == WrappedInUseEntity.UseEntityAction.ATTACK) {
                this.hitTicks = 0;
                lastTarget = target;
                target = wrapped.getHurtEntity(getData().getPlayer().getWorld());
            }
            this.lastUseEntity = System.currentTimeMillis();

        } else if(isFlyingPacket(event.getPacketType())) {
            this.hitTicks++;
        }

    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {

    }
}

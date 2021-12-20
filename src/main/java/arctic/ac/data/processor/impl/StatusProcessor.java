package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import arctic.ac.utils.ArcticPotionEffect;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.out.impl.WrappedOutEntityEffect;
import eu.salers.salty.packet.wrappers.play.out.impl.WrappedOutRemoveEntityEffect;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.LinkedList;
import java.util.List;

@Getter
public class StatusProcessor extends Processor {

    private final List<ArcticPotionEffect> potionEffects = new LinkedList<>();
    private final List<GameMode> possibleGamemodes = new LinkedList<>();

    public StatusProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {

    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {
        if (event.getPacketType() == PacketType.OUT_ENTITY_EFFECT) {
            final WrappedOutEntityEffect wrapped = new WrappedOutEntityEffect(event.getPacket());

            if (wrapped.getEntityId() != getData().getPlayer().getEntityId()) return;

            getData().getTransactionHandler().preTransaction(() ->
                    potionEffects.add(new ArcticPotionEffect(wrapped.getAmplifier(), wrapped.getEffectType())));


        } else if (event.getPacketType() == PacketType.OUT_REMOVE_ENTITY_EFFECT) {
            final WrappedOutRemoveEntityEffect wrapped = new WrappedOutRemoveEntityEffect(event.getPacket());

            if (wrapped.getEntityId() != getData().getPlayer().getEntityId()) return;

            getData().getTransactionHandler().postTransaction(() -> potionEffects.remove(potionEffects.stream().
                    filter(effect -> effect.getType() == wrapped.getEffectType()).findAny().get()));




        }

    }
}

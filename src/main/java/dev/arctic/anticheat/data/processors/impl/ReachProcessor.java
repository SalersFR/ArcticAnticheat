package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.data.tracking.EntityTracker;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.PacketEvent;

public class ReachProcessor extends Processor {
    public ReachProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(data.getCombatProcessor().getHitTicks() >= 10) return;
        final EntityTracker entityTracker = data.getEntityTracker();
        if(event.getPacket().isFlying()) {
            entityTracker.interpolate(data.getCombatProcessor().getTarget().getEntityId());
        }

    }

    @Override
    public void handleSending(PacketEvent event) {
        final Packet packet = event.getPacket();
        final EntityTracker entityTracker = data.getEntityTracker();
        if(packet.getType() == PacketType.Play.Server.ENTITY_TELEPORT) {
            final WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(packet);
            entityTracker.teleport(wrapper.getEntityID(), wrapper.getX(), wrapper.getY(), wrapper.getZ());

        } else if(packet.getType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            final WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
            entityTracker.addEntity(wrapper.getEntityID(), wrapper.getX(), wrapper.getY(), wrapper.getZ());

        } else if(packet.getType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            final WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn(packet);
            entityTracker.addEntity(wrapper.getEntityID(), wrapper.getX(), wrapper.getY(), wrapper.getZ());

        } else if(packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            final WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook(packet);
            entityTracker.relMove(wrapper.getEntityID(), wrapper.getDx(), wrapper.getDy(), wrapper.getDz());
        } else if(packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE) {
            final WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove(packet);
            entityTracker.relMove(wrapper.getEntityID(), wrapper.getDx(), wrapper.getDy(), wrapper.getDz());
        }

    }
}

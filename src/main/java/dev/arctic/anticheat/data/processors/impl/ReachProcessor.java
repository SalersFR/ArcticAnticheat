package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Data;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * DON'T TOUCH UNLESS I ASKED
 */
public class ReachProcessor extends Processor {

    private final List<ReachEntity> reachEntityList = new ArrayList<>();

    public ReachProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isFlying()) {
            for (ReachEntity reachEntity : reachEntityList) {
                reachEntity.interpolate();

            }
        }

    }

    @Override
    public void handleSending(PacketEvent event) {
        final Packet packet = event.getPacket();
        final PacketType type = packet.getType();

        if (type == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            final WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving(packet);
            spawn(wrapper.getEntityID(), wrapper.getPos());

        } else if (type == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            final WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn(packet);
            spawn(wrapper.getEntityID(), wrapper.getPosition());

        } else if (type == PacketType.Play.Server.REL_ENTITY_MOVE) {
            final WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove(packet);
            move(true, wrapper.getMove(), wrapper.getEntityID());

        } else if (type == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            final WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook(packet);
            move(true, wrapper.getMove(), wrapper.getEntityID());

        } else if (type == PacketType.Play.Server.ENTITY_TELEPORT) {
            final WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(packet);
            move(false, wrapper.getPos(), wrapper.getEntityID());
        }

    }

    private void spawn(final int id, final Vector pos) {
        this.reachEntityList.add(new ReachEntity(id, pos));
    }

    private void move(final boolean rel, final Vector move, final int id) {
        if (rel)
            data.getTransactionProcessor().todoTransaction(() -> {
                getEntity(id).getRel().add(move);
                getEntity(id).setFlyingSteps(3);
            });
        else
            data.getTransactionProcessor().todoTransaction(() -> {
                getEntity(id).setRel(move);
                getEntity(id).setFlyingSteps(3);
            });
    }

    private ReachEntity getEntity(final int id) {
        return reachEntityList.stream().filter(reachEntity -> reachEntity.getId() == id).findAny().get();
    }

    @Data
    public class ReachEntity {

        private Vector position;
        private Vector rel;

        private int id, flyingSteps;

        public ReachEntity(int id, Vector pos) {
            this.id = id;
            this.rel = position = pos;
        }

        public void interpolate() {
            if (flyingSteps > 0) {
                position.setX(position.getX() + (rel.getX() - position.getX()) / flyingSteps);
                position.setY(position.getY() + (rel.getY() - position.getY()) / flyingSteps);
                position.setZ(position.getZ() + (rel.getZ() - position.getZ()) / flyingSteps);
                flyingSteps--;
            }
        }
    }
}

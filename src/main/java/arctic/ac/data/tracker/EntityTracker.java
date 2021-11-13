package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import arctic.ac.utils.AEntity;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMove;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class EntityTracker {

    private final PlayerData data;

    @Getter
    private final Set<AEntity> trackedEntities = new HashSet<>();




    public void handleNamedSpawn(final WrapperPlayServerNamedEntitySpawn packet) {
        this.trackedEntities.add(new AEntity(packet.getX(), packet.getY(), packet.getZ(), packet.getEntityID()));
    }

    public void handleRelMove(final WrapperPlayServerRelEntityMove packet) {
        for (final AEntity entities : this.trackedEntities) {
            if (entities.getId() == packet.getEntityID()) {
                entities.relMove(packet.getDx(), packet.getDy(), packet.getDz());
            }
        }
    }

    public void handleRelMoveLook(final WrapperPlayServerRelEntityMoveLook packet) {
        for (final AEntity entities : this.trackedEntities) {
            if (entities.getId() == packet.getEntityID()) {
                entities.relMove(packet.getDx(), packet.getDy(), packet.getDz());
            }
        }
    }

    public void handleTeleport(final WrapperPlayServerEntityTeleport packet) {
        for (final AEntity entities : this.trackedEntities) {
            if (entities.getId() == packet.getEntityID()) {
                entities.teleport(packet.getX(), packet.getY(), packet.getZ());
            }
        }
    }


}

package honeybadger.ac.data.impl;

import com.comphenix.packetwrapper.*;
import honeybadger.ac.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TargetTracker {


    private final PlayerData data;
    private double x, y, z;
    private List<Entity> entityList = new ArrayList<>();


    public TargetTracker(PlayerData data) {
        this.data = data;
    }

    public void handleRelMove(WrapperPlayServerRelEntityMove wrapper) {

        if (data.getInteractData().getTarget() != null) {
            for (Entity entities : entityList) {
                if (entities.getEntityId() == data.getInteractData().getTarget().getEntityId()) {


                    final double x = wrapper.getDx() / 32;
                    final double y = wrapper.getDy() / 32;
                    final double z = wrapper.getDz() / 32;

                    this.x += x;
                    this.y += y;
                    this.z += z;


                }
            }
        }
    }

    public void handleTeleport(WrapperPlayServerEntityTeleport wrapper) {
        if (data.getInteractData().getTarget() != null) {
            for (Entity entities : entityList) {
                if (entities.getEntityId() == data.getInteractData().getTarget().getEntityId()) {

                    this.x = wrapper.getX() / 32D;
                    this.y = wrapper.getY() / 32D;
                    this.z = wrapper.getZ() / 32D;
                }
            }
        }
    }

    public void handleSpawn(WrapperPlayServerSpawnEntityLiving wrapper) {
        this.entityList.add(wrapper.getEntity(data.getPlayer().getWorld()));

        if (data.getInteractData().getTarget() != null) {
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x = wrapper.getX() / 32D;
                this.y = wrapper.getY() / 32D;
                this.z = wrapper.getZ() / 32D;

            }
        }


    }

    public void handleRelMoveLook(WrapperPlayServerRelEntityMoveLook wrapper) {
        if (data.getInteractData().getTarget() != null) {
            for (Entity entities : entityList) {
                if (entities.getEntityId() == data.getInteractData().getTarget().getEntityId()) {


                    final double x = wrapper.getDx() / 32;
                    final double y = wrapper.getDy() / 32;
                    final double z = wrapper.getDz() / 32;

                    this.x += x;
                    this.y += y;
                    this.z += z;


                }
            }
        }


    }

    public void handleNamedEntitySpawn(WrapperPlayServerNamedEntitySpawn wrapper) {

        this.entityList.add(wrapper.getEntity(data.getPlayer().getWorld()));

        if (data.getInteractData().getTarget() != null) {
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x = wrapper.getX() / 32D;
                this.y = wrapper.getY() / 32D;
                this.z = wrapper.getZ() / 32D;

            }
        }

    }
}

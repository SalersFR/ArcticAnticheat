package honeybadger.ac.data.impl;

import com.comphenix.packetwrapper.*;
import honeybadger.ac.data.PlayerData;
import lombok.Getter;

@Getter
public class TargetTracker {

    private final PlayerData data;
    private double x, y, z;

    public TargetTracker(PlayerData data) {
        this.data = data;
    }

    public void handleRelMove(WrapperPlayServerRelEntityMove wrapper) {

        if (data.getInteractData().getTarget() != null) {
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x += wrapper.getDx() / 32D;
                this.y += wrapper.getDy() / 32D;
                this.z += wrapper.getDz() / 32D;


            }
        }
    }

    public void handleTeleport(WrapperPlayServerEntityTeleport wrapper) {
        if (data.getInteractData().getTarget() != null) {
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x = wrapper.getX() / 32D;
                this.y = wrapper.getY() / 32D;
                this.z = wrapper.getZ() / 32D;
            }
        }
    }

    public void handleSpawn(WrapperPlayServerSpawnEntityLiving wrapper) {

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
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x += wrapper.getDx() / 32D;
                this.y += wrapper.getDy() / 32D;
                this.z += wrapper.getDz() / 32D;


            }
        }


    }

    public void handleNamedEntitySpawn(WrapperPlayServerNamedEntitySpawn wrapper) {

        if (data.getInteractData().getTarget() != null) {
            if (data.getInteractData().getTarget().getEntityId() == wrapper.getEntityID()) {

                this.x = wrapper.getX() / 32D;
                this.y = wrapper.getY() / 32D;
                this.z = wrapper.getZ() / 32D;

            }
        }

    }
}

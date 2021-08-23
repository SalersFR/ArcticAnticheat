package polar.ac.data.impl;

import com.comphenix.packetwrapper.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import polar.ac.data.PlayerData;
import polar.ac.utils.PEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TargetTracker {

    private final PlayerData data;




    private List<PEntity> entities = new ArrayList<>();

    public TargetTracker(PlayerData data) {
        this.data = data;
    }

    public void handleRelMove(WrapperPlayServerRelEntityMove wrapper) {

        for (PEntity entity : entities) {
             if(entity.getId() == wrapper.getEntityID()) {


                 final double x = wrapper.getDx() / 32;
                 final double y = wrapper.getDy() / 32;
                 final double z = wrapper.getDz() / 32;

                 entity.addX(x);
                 entity.addY(y);
                 entity.addZ(z);
             }


        }

    }

    public void handleTeleport(WrapperPlayServerEntityTeleport wrapper) {
        for(PEntity entity : entities) {
            if(entity.getId() == wrapper.getEntityID()) {

                final double x = wrapper.getX() / 32;
                final double y = wrapper.getY() / 32;
                final double z = wrapper.getZ() / 32;

                entity.setX(x);
                entity.setY(y);
                entity.setZ(z);

            }
        }
    }

    public void handleSpawn(WrapperPlayServerSpawnEntityLiving wrapper) {


        double x = wrapper.getX() / 32D;
        double y = wrapper.getY() / 32D;
        double z = wrapper.getZ() / 32D;

        entities.add(new PEntity(x, y, z, wrapper.getEntity(data.getPlayer().getWorld()).getType(), wrapper.getEntityID()));


    }


    public void handleRelMoveLook(WrapperPlayServerRelEntityMoveLook wrapper) {

        for (PEntity entity : entities) {
            if(entity.getId() == wrapper.getEntityID()) {


                final double x = wrapper.getDx() / 32;
                final double y = wrapper.getDy() / 32;
                final double z = wrapper.getDz() / 32;

                entity.addX(x);
                entity.addY(y);
                entity.addZ(z);
            }


        }


    }

    public void handleNamedEntitySpawn(WrapperPlayServerNamedEntitySpawn wrapper) {

        double x = wrapper.getX() / 32D;
        double y = wrapper.getY() / 32D;
        double z = wrapper.getZ() / 32D;

        entities.add(new PEntity(x, y, z, wrapper.getEntity(data.getPlayer().getWorld()).getType(), wrapper.getEntityID()));


    }


}

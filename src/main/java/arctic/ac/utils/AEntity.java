package arctic.ac.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@Data

public class AEntity {

    private double x, y, z, newX, newY, newZ, relX, relY, relZ;
    private EntityType type;
    private int id, interpolationSteps;
    private short transactionID = -900;

    public AEntity(double x, double y, double z, int id) {
        this.x = x / 32;
        this.y = y / 32;
        this.z = z / 32;
        this.id = id;
    }

    public void relMove(final double x, final double y, double z) {

        this.relX += (x / 32);
        this.relY += (y / 32);
        this.relZ += (z / 32);

        sendTransaction();


    }

    public Entity getEntity() {
        for (World worlds : Bukkit.getWorlds()) {
            for (Entity entities : worlds.getEntities()) {
                if (entities.getEntityId() == id)
                    return entities;
            }
        }
        return null;

    }

    public void teleport(final double x, final double y, final double z) {

        this.relX = (x / 32);
        this.relY = (y / 32);
        this.relZ = (z / 32);

        sendTransaction();
    }

    public void sendTransaction() {

        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);

        packet.getBooleans().write(0, false);
        packet.getShorts().write(0, transactionID);

        this.transactionID++;

        if (transactionID >= -10)
            transactionID = -900;

        packet.getIntegers().write(0, getId());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket((Player) getEntity(), packet);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void interpolate() {
        if (interpolationSteps > 0) {

            x = (x + (newX - x)) / interpolationSteps;
            y = (y + (newY - y)) / interpolationSteps;
            z = (z + (newZ - z)) / interpolationSteps;

            --interpolationSteps;
        }

    }

    public void handleTransaction() {
        this.interpolationSteps = 3;

        newX = relX;
        newY = relY;
        newZ = relZ;
    }
}

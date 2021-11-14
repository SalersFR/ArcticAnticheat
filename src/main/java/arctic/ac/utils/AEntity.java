package arctic.ac.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Data

public class AEntity {

    private double x, y, z, newX, newY, newZ, relX, relY, relZ;
    private EntityType type;
    private int id, interpolationSteps;
    private short transactionID = -900;
    private HashMap<Short, Vector> transactionTimes = new HashMap<>();

    public AEntity(double x, double y, double z, int id) {
        this.x = x / 32;
        this.y = y / 32;
        this.z = z / 32;
        this.id = id;
    }

    public void relMove(final double x, final double y, double z) {

        double relX = (x / 32);
        double relY = (y / 32);
        double relZ = (z / 32);
        this.relX = relX;
        this.relY = relY;
        this.relZ = relZ;

        //Bukkit.broadcastMessage("relX1 " + this.relX);

        sendTransaction(new Vector(relX,relY,relZ));
        interpolationSteps = 3;

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

        double relX = (x / 32);
        double relY = (y / 32);
        double relZ = (z / 32);
        this.relX = relX;
        this.relY = relY;
        this.relZ = relZ;

        sendTransaction(new Vector(relX,relY,relZ));
    }

    public void sendTransaction(Vector loc) {

        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);

        packet.getBooleans().write(0, false);
        packet.getShorts().write(0, transactionID);

        transactionTimes.put(transactionID,loc);

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

            x = x + ((newX)) / interpolationSteps;
            y = y + ((newY)) / interpolationSteps;
            z = z + ((newZ)) / interpolationSteps;

            //Bukkit.broadcastMessage("motionX" + newX);

            --interpolationSteps;
        }

    }

    public void handleTransaction(short id) {
        this.interpolationSteps = 3;

        Vector transVector = transactionTimes.get(id);

        newX = relX;
        newY = relY;
        newZ = relZ;

        Bukkit.broadcastMessage("relX2 " + this.relX);
    }
}

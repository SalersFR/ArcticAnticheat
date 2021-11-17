package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class EntityTracker {

    private PlayerData player;
    public HashMap<Integer, ReachEntity> getEntityFromId = new HashMap<>();

    public EntityTracker(PlayerData p) {
        this.player = p;
    }

    public void addEntity(int id, double x, double y, double z) {
        getEntityFromId.put(id, new ReachEntity(id, x / 32D, y / 32D, z / 32D));
    }

    public void relMove(int id, final double x, final double y, double z) {
        //TODO send Transaction
        sendTransaction(id, new Vector(getEntityFromId.get(id).getX() + (x / 32), getEntityFromId.get(id).getY()
                + (y / 32), getEntityFromId.get(id).getZ() + (z / 32)));
    }

    public void teleport(int id, final double x, final double y, final double z) {

        double relX = (x / 32);
        double relY = (y / 32);
        double relZ = (z / 32);
        getEntityFromId.get(id).setNewX(x / 32);
        getEntityFromId.get(id).setNewY(y / 32);
        getEntityFromId.get(id).setNewZ(z / 32);

        getEntityFromId.get(id).setInterpolationSteps(1);

        //TODO sendRelMoveNewLater
        sendTransaction(id, new Vector(relX, relY, relZ));
    }

    public void sendTransaction(int id, Vector loc) {

        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);

        packet.getBooleans().write(0, false);
        packet.getShorts().write(0, getEntityFromId.get(id).getTransactionID());

        getEntityFromId.get(id).getTransactionTimes().put(getEntityFromId.get(id).getTransactionID(), loc);

        getEntityFromId.get(id).setTransactionID((short) (getEntityFromId.get(id).getTransactionID() + 1));

        if (getEntityFromId.get(id).getTransactionID() >= -10)
            getEntityFromId.get(id).setTransactionID((short) -900);

        packet.getIntegers().write(0, id);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player.getPlayer(), packet);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void interpolate(int id) {
        if (getEntityFromId.get(id).getInterpolationSteps() > 0) {

            getEntityFromId.get(id).setX(getEntityFromId.get(id).getX() + (getEntityFromId.get(id).getNewX()) / getEntityFromId.get(id).getInterpolationSteps());
            getEntityFromId.get(id).setY(getEntityFromId.get(id).getY() + (getEntityFromId.get(id).getNewY()) / getEntityFromId.get(id).getInterpolationSteps());
            getEntityFromId.get(id).setZ(getEntityFromId.get(id).getZ() + (getEntityFromId.get(id).getNewZ()) / getEntityFromId.get(id).getInterpolationSteps());

            getEntityFromId.get(id).setInterpolationSteps(getEntityFromId.get(id).getInterpolationSteps() - 1);
        }

    }

    public void handleTransaction(int id, short transID) {
        getEntityFromId.get(id).setInterpolationSteps(3);

        Vector transVector = getEntityFromId.get(id).getTransactionTimes().get(transID);

        getEntityFromId.get(id).setNewX(transVector.getX());
        getEntityFromId.get(id).setNewY(transVector.getY());
        getEntityFromId.get(id).setNewZ(transVector.getZ());
    }
}

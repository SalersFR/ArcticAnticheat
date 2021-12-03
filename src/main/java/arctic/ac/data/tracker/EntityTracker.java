package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class EntityTracker {

    public HashMap<Integer, ReachEntity> getEntityFromId = new HashMap<>();
    private PlayerData player;

    public EntityTracker(PlayerData p) {
        this.player = p;
    }

    public void addEntity(int id, double x, double y, double z) {
        getEntityFromId.put(id, new ReachEntity(id, x / 32D, y / 32D, z / 32D));
    }

    public void relMove(int id, final double x, final double y, double z) {
        //TODO send Transaction
        sendTransaction(id, new Vector(getEntityFromId.get(id).getX() + (x), getEntityFromId.get(id).getY()
                + (y), getEntityFromId.get(id).getZ() + (z)));
    }

    public void teleport(int id, final double x, final double y, final double z) {

        double relX = (x);
        double relY = (y);
        double relZ = (z);
        getEntityFromId.get(id).setNewX(x);
        getEntityFromId.get(id).setNewY(y);
        getEntityFromId.get(id).setNewZ(z);

        getEntityFromId.get(id).setInterpolationSteps(1);

        //TODO sendRelMoveNewLater
        sendTransaction(id, new Vector(relX, relY, relZ));
    }

    public void sendTransaction(int id, Vector loc) {

        PacketEvents.get().getPlayerUtils().sendPacket(player.getPlayer(),
                new WrappedPacketOutTransaction(id, getEntityFromId.get(id).getTransactionID(), false));


        getEntityFromId.get(id).getTransactionTimes().put(getEntityFromId.get(id).getTransactionID(), loc);

        getEntityFromId.get(id).setTransactionID((short) (getEntityFromId.get(id).getTransactionID() + 1));

        if (getEntityFromId.get(id).getTransactionID() >= -10)
            getEntityFromId.get(id).setTransactionID((short) -900);


    }

    public void interpolate(int id) {
        if (getEntityFromId.get(id).getInterpolationSteps() > 0) {

            getEntityFromId.get(id).setX(getEntityFromId.get(id).getX() + (getEntityFromId.get(id).getNewX() - getEntityFromId.get(id).getX() ) / getEntityFromId.get(id).getInterpolationSteps());
            getEntityFromId.get(id).setY(getEntityFromId.get(id).getY() + (getEntityFromId.get(id).getNewY() - getEntityFromId.get(id).getY()) / getEntityFromId.get(id).getInterpolationSteps());
            getEntityFromId.get(id).setZ(getEntityFromId.get(id).getZ() + (getEntityFromId.get(id).getNewZ() - getEntityFromId.get(id).getZ()) / getEntityFromId.get(id).getInterpolationSteps());

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

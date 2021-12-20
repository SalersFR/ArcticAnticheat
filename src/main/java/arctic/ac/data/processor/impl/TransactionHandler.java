package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TransactionHandler {
    private short transactionID;
    private final Map<Short, Long> transactionTimes = new HashMap<>();
    private PlayerData data;
    private HashMap<Short,Runnable> todo = new HashMap<>();

    public TransactionHandler(PlayerData data) {
        this.data = data;
    }

    //TODO if you want to get the player ping call this method every tick
    public void onTick() {
        if (++transactionID > 0) transactionID = Short.MIN_VALUE;
        transactionTimes.put(transactionID, System.currentTimeMillis());

        ((CraftPlayer) data.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutTransaction(0, transactionID, false));
    }

    public void todoTransition(Runnable runnable) {
        if (++transactionID > 0) // don't want to interfere with actual transaction packets
            transactionID = Short.MIN_VALUE;

        transactionTimes.put(transactionID, -5L);
        todo.put(transactionID,runnable);

        ((CraftPlayer) data.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutTransaction(0, transactionID, false));
    }


    public void onTransaction(PlayerData data, short id) {
        if (id > 0)
            return;

        if (transactionTimes.containsKey(id)) {
            if (transactionTimes.get(id) == -5L) {
                todo.get(id).run();
            } else {
                long ping = System.currentTimeMillis() - transactionTimes.get(id);
                //TODO setTransaction ping to ping.
            }
        }
    }
}

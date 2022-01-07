package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientTransaction;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class TransactionProcessor extends Processor {


    private short transactionID;
    private final Map<Short, Long> transactionTimes = new HashMap<>();
    private HashMap<Short, Runnable> todo = new HashMap<>();

    public TransactionProcessor(PlayerData data) {
        super(data);
    }


    public void todoTransaction(Runnable runnable) {
        if (++transactionID > 0)
            transactionID = Short.MIN_VALUE;

        transactionTimes.put(transactionID, -5L);
        todo.put(transactionID, runnable);

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);
        packet.getBooleans().write(0, false);
        packet.getShorts().write(0, transactionID);
        packet.getIntegers().write(0, 0);


        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), packet);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }


    public void onTransaction(final PacketEvent event) {
        if (event.getPacket().isInTransaction()) {
            final WrapperPlayClientTransaction packet = new WrapperPlayClientTransaction(event.getPacket());
            final short id = packet.getActionNumber();


            if (id > 0)
                return;

            if (transactionTimes.containsKey(id)) {
                if (transactionTimes.get(id) == -5L) {
                    todo.get(id).run();
                }
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        data.getPlayer().kickPlayer("Invalid Transaction");
                    }
                }.runTask(Arctic.getInstance().getPlugin());
            }
        }

    }


    @Override
    public void handleReceive(PacketEvent event) {
        onTransaction(event);

    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

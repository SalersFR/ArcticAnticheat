package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientTransaction;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TransactionProcessor extends Processor {


    private short transactionID;
    private final Map<Short, Long> transactionTimes = new HashMap<>();
    private HashMap<Short, List<Runnable>> todo = new HashMap<>();
    private boolean invalidTransactionReply;

    public TransactionProcessor(PlayerData data) {
        super(data);
    }


    public void todoTransaction(Runnable runnable) {
        if (++transactionID > 0)
            transactionID = Short.MIN_VALUE;

        transactionTimes.put(transactionID, -5L);


        if (!this.todo.containsKey(transactionID)) {
            this.todo.put(transactionID, new ArrayList<>());
        }
        this.todo.get(transactionID).add(runnable);

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
                    todo.get(id).forEach(Runnable::run);
                    invalidTransactionReply = false;
                    transactionTimes.remove(id);
                }
            } else {
               invalidTransactionReply = true;
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

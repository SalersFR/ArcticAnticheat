package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.packetwrapper.WrapperPlayClientTransaction;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.lang.reflect.InvocationTargetException;

public class BadPacketsE extends Check {


    private int ticksSinceLastTransactionConfirm;

    public BadPacketsE(PlayerData data) {
        super(data, "BadPackets", "E", "player.badpackets.e", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketEvent) {

            final PacketEvent event = (PacketEvent) e;
            final PacketType type = event.getPacketType();


            if (type == PacketType.Play.Client.TRANSACTION) {
                final WrapperPlayClientTransaction wrapperPlayClientTransaction = new WrapperPlayClientTransaction(event.getContainer());
                debug("ticks=" + this.ticksSinceLastTransactionConfirm + "currentID=" + wrapperPlayClientTransaction.getWindowId());
                this.ticksSinceLastTransactionConfirm = 0;
            }


        } else if (e instanceof FlyingEvent) {

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);
            packet.getBooleans().write(0, false);
            packet.getShorts().write(0, (short) 0);
            packet.getIntegers().write(0, 0);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), packet);
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }

            debug("ticks=" + this.ticksSinceLastTransactionConfirm);
            if (++this.ticksSinceLastTransactionConfirm > (47 + ((CraftPlayer) data.getPlayer()).getHandle().ping * 0.87F)&&  data.getInteractData().getTicksAlive() > 100) {
                if (++buffer > 1) {
                    fail("ticks=" + this.ticksSinceLastTransactionConfirm);
                }

            } else if (buffer > 0) buffer = 0;

        }

    }
}

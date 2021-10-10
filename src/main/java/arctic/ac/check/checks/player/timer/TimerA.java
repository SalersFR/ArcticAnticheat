package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketEvent;
import arctic.ac.event.server.ServerPositionEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.lang.reflect.InvocationTargetException;

public class TimerA extends Check {
    private long lastTickTime;
    private double balance;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", true);
    }

    @Override
    public void handle(Event e) {



       if(e instanceof PacketEvent) {
            final PacketEvent event = (PacketEvent) e;
            if(event.getPacketType() == PacketType.Play.Client.TRANSACTION) {
                final int ping = ((CraftPlayer) data.getPlayer()).getHandle().ping;
                this.balance -= ping * 0.05F;
                debug("balance=" + balance);
            } else if(event.getPacketType() == PacketType.Play.Client.FLYING ||
                    event.getPacketType() == PacketType.Play.Client.POSITION ||
                    event.getPacketType() == PacketType.Play.Client.POSITION_LOOK ||
                    event.getPacketType() == PacketType.Play.Client.LOOK) {



                balance++;

                PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);
                packet.getBooleans().write(0, false);
                packet.getShorts().write(0, (short) 0);
                packet.getIntegers().write(0, 0);

                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), packet);
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }






            }

        }
    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}

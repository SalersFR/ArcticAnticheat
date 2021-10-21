package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.NetworkProcessor;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import java.lang.reflect.InvocationTargetException;

public class TimerA extends Check {

    private double balance = 20D;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up time.", true);
    }


    // TODO redo this

    @Override
    public void handle(Event e) {
        if (e instanceof PacketEvent) {

            final PacketEvent event = (PacketEvent) e;
            final PacketType type = event.getPacketType();

            if (type.equals(PacketType.Play.Client.CUSTOM_PAYLOAD)) {
                balance = 0;

            }


        } else if (e instanceof FlyingEvent) {


            final NetworkProcessor networkProcessor = data.getNetworkProcessor();

            //using transaction ping cuz it's more accurate.
            debug("balance=" + balance + " transactionPing=" + networkProcessor.getTransactionPing());

            //sending payload every tick
            final PacketContainer payload = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
            payload.getStrings().write(0,"jaj");

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), payload);
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }


            //increasing balance
            this.balance++;
        }


    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}

package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;

@Getter
@RequiredArgsConstructor
public class NetworkProcessor {

    private final PlayerData data;
    private short transactionID = 2000;
    private long lastTransactionSent, lastTransactionConfirm, lastKeepAliveSent, lastKeepAliveConfirm; //confirm = PlayIn , sent = PlayOut
    private int transactionPing, keepAlivePing;

    private long time() {
        return System.currentTimeMillis();
    }

    public void handleIn(final PacketEvent event) {

        final PacketType type = event.getPacketType();
        final PacketContainer packet = event.getPacket();

        if (type.equals(PacketType.Play.Client.FLYING) ||
                type.equals(PacketType.Play.Client.POSITION) ||
                type.equals(PacketType.Play.Client.LOOK) ||
                type.equals(PacketType.Play.Client.POSITION_LOOK)) {

            final PacketContainer transactionPacket = new PacketContainer(PacketType.Play.Server.TRANSACTION);

            transactionPacket.getBooleans().write(0, false);
            transactionPacket.getShorts().write(0, this.transactionID);
            transactionPacket.getIntegers().write(0, 0);

            transactionID--;
            this.lastTransactionSent = time();

            if (transactionID <= 1200)
                transactionID = 2000;

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), transactionPacket);
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }

        } else if (type.equals(PacketType.Play.Client.TRANSACTION)) {
            if (packet.getShorts().read(0).equals(this.transactionID)) {
                this.lastTransactionConfirm = time();
                this.transactionPing = (int) (this.lastTransactionConfirm - this.lastTransactionSent);
            }

        } else if (type.equals(PacketType.Play.Client.KEEP_ALIVE)) {
            this.lastKeepAliveConfirm = time();
            this.keepAlivePing = (int) (this.lastKeepAliveConfirm - this.lastTransactionSent);
        }
    }

    public void handleOut(final PacketEvent event) {
        final PacketType type = event.getPacketType();
        final PacketContainer packet = event.getPacket();

        if (type.equals(PacketType.Play.Server.TRANSACTION)) {
            this.lastTransactionSent = time();


        } else if(type.equals(PacketType.Play.Server.KEEP_ALIVE)) {
            this.lastKeepAliveSent = time();
        }

    }

}

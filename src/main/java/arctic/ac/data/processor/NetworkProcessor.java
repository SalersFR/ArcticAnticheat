package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NetworkProcessor {

    private final PlayerData data;
    private short transactionID = 2000;
    private long lastTransactionSent, lastTransactionConfirm, lastKeepAliveSent, lastKeepAliveConfirm, lastFlying; //confirm = PlayIn , sent = PlayOut
    private int transactionPing, keepAlivePing;
    private ClientVersion clientVersion;
    private String clientBrand;

    private long time() {
        return System.currentTimeMillis();
    }

    public void handleIn(final PacketPlayReceiveEvent event) {

        final byte type = event.getPacketId();


        if (type == PacketType.Play.Client.FLYING ||
                type == PacketType.Play.Client.POSITION ||
                type == PacketType.Play.Client.LOOK ||
                type == PacketType.Play.Client.POSITION_LOOK) {

            PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(),
                    new WrappedPacketOutTransaction(0, transactionID, false));

            transactionID--;
            this.lastTransactionSent = time();
            this.lastFlying = time();

            if (transactionID <= 1200)
                transactionID = 2000;


        } else if (type == PacketType.Play.Client.TRANSACTION) {
            final WrappedPacketInTransaction packet = new WrappedPacketInTransaction(event.getNMSPacket());
            if (packet.getActionNumber() == transactionID) {
                this.lastTransactionConfirm = time();
                this.transactionPing = (int) (this.lastTransactionConfirm - this.lastTransactionSent);
            }

        } else if (type == PacketType.Play.Client.KEEP_ALIVE) {
            this.lastKeepAliveConfirm = time();
            this.keepAlivePing = (int) (this.lastKeepAliveConfirm - this.lastTransactionSent);
        }
    }

    public void handleOut(final PacketPlaySendEvent event) {
        final byte type = event.getPacketId();


        if (type == PacketType.Play.Server.TRANSACTION) {
            this.lastTransactionSent = time();


        } else if (type == PacketType.Play.Server.KEEP_ALIVE) {
            this.lastKeepAliveSent = time();
        }

    }

}

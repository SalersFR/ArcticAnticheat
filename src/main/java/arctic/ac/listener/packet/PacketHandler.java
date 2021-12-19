package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.event.listener.SaltyPacketListener;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.WrappedInPacket;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;

public class PacketHandler extends SaltyPacketListener {


    @Override
    public void onPacketInReceive(SaltyPacketInReceiveEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return;

        Arctic.INSTANCE.getDataThread().execute(() -> data.handleReceive(event));
        if (event.getPacketType().equals(PacketType.IN_TRANSACTION)) {
            WrappedInTransaction wrapper = new WrappedInTransaction(event.getPacket());
            data.getTransactionHandler().onTransaction(data,wrapper.getActionId());
        }
    }

    @Override
    public void onPacketOutSend(SaltyPacketOutSendEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return;

        Arctic.INSTANCE.getDataThread().execute(() -> data.handleSending(event));
    }
}

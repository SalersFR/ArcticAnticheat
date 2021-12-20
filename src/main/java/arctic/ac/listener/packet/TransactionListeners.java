package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import dev.thomazz.pledge.api.event.TransactionEvent;
import dev.thomazz.pledge.api.event.TransactionListener;

public class TransactionListeners implements TransactionListener {

    @Override
    public void onReceiveStart(TransactionEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getInfo().getPlayer());
        if(data == null) return;
        data.getTransactionHandler().handleReceive(event);


    }

    @Override
    public void onReceiveEnd(TransactionEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getInfo().getPlayer());
        if(data == null) return;
        data.getTransactionHandler().handleReceive(event);

    }

    @Override
    public void onSendStart(TransactionEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getInfo().getPlayer());
        if(data == null) return;
        data.getTransactionHandler().setIds(event);

    }

    @Override
    public void onSendEnd(TransactionEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getInfo().getPlayer());
        if(data == null) return;


    }


}

package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import dev.thomazz.pledge.api.event.TransactionEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Data
public class TransactionHandler {

    private final PlayerData data;

    private final Map<Integer, List<Runnable>> transactionMap = new HashMap<>();
    private int preId, postId;

    public void setIds(final TransactionEvent event) {
        this.preId = event.getTransactionPair().getId1();
        this.postId = preId + 1;
    }

    public void preTransaction(final Runnable runnable) {
        final Optional<List<Runnable>> list = Optional.ofNullable(transactionMap.get(preId));
        if(list.isPresent()) {
            list.get().add(runnable);
        } else {
            final List<Runnable> runnables = new ArrayList<>();
            runnables.add(runnable);
            transactionMap.put(preId, runnables);
        }

    }

    public void postTransaction(final Runnable runnable) {
        final Optional<List<Runnable>> list = Optional.ofNullable(transactionMap.get(postId));
        if(list.isPresent()) {
            list.get().add(runnable);
        } else {
            final List<Runnable> runnables = new ArrayList<>();
            runnables.add(runnable);
            transactionMap.put(postId, runnables);
        }
    }

    public void handleReceive(final TransactionEvent event) {
        transactionMap.get(event.getTransactionPair().getId1()).forEach(Runnable::run);
        transactionMap.remove(event.getTransactionPair().getId1());
    }





}
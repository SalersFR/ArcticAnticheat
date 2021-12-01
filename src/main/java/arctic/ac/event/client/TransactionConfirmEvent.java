package arctic.ac.event.client;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import lombok.Getter;

@Getter
public class TransactionConfirmEvent extends Event {


    private final WrappedPacketInTransaction wrapper;

    public TransactionConfirmEvent(WrappedPacketInTransaction wrapper) {
        this.wrapper = wrapper;
    }


}

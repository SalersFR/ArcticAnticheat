package arctic.ac.event.client;

import arctic.ac.event.Event;
import com.comphenix.packetwrapper.WrapperPlayClientTransaction;

public class TransactionConfirmEvent extends Event {


    private final WrapperPlayClientTransaction wrapper;

    public TransactionConfirmEvent(WrapperPlayClientTransaction wrapper) {
        this.wrapper = wrapper;
    }

    
}

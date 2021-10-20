package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.NetworkProcessor;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.TransactionConfirmEvent;
import arctic.ac.event.server.ServerPositionEvent;

public class TimerA extends Check {

    private double balance = 20D;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up time.", true);
    }


    // TODO redo this

    @Override
    public void handle(Event e) {
        if (e instanceof ServerPositionEvent) {


            final NetworkProcessor networkProcessor = data.getNetworkProcessor();

            this.balance -= getMillis(networkProcessor.getLastFlying());
            debug("out position sent !");

            //sent a transaction confirm, so changing balance


        } else if (e instanceof FlyingEvent) {


            final NetworkProcessor networkProcessor = data.getNetworkProcessor();

            //using transaction ping cuz it's more accurate.
            debug("balance=" + balance + " transactionPing=" + networkProcessor.getTransactionPing());

            //finally setting the balance as it was at the start
            this.balance += 0.01;
        }


    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}

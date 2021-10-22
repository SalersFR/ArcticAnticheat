package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.NetworkProcessor;
import arctic.ac.event.Event;
import arctic.ac.event.client.TransactionConfirmEvent;
import org.bukkit.Bukkit;

public class TimerA extends Check {

    private int lastFlyingDelta;


    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is changing packets rate.", true);
    }

    @Override
    public void handle(Event e) {

        if (e instanceof TransactionConfirmEvent) {

            final NetworkProcessor networkProcessor = data.getNetworkProcessor();
            final int flyingDelta = (int) (System.currentTimeMillis() - networkProcessor.getLastFlying());

            final int lastFlyingDelta = this.lastFlyingDelta;
            this.lastFlyingDelta = flyingDelta;

            final int difference = flyingDelta - lastFlyingDelta;

            debug("fD=" + flyingDelta + " diff=" + difference + " buffer=" + buffer);

            if(buffer < 0) buffer = 0;

            final boolean exempt = data.getInteractData().getTicksAlive() < 35;

            if(difference >= 10 && !exempt) {
                if(buffer < 10.5) buffer++;
                if(buffer > 7)
                    fail("diff=" + difference + " buffer=" + buffer);
            } else if(buffer > 0) buffer -= 0.1D;




        }
    }
}

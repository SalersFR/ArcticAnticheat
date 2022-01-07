package dev.arctic.anticheat.check.impl.player.timer;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class TimerA extends Check {

    private long lastFlying = System.currentTimeMillis();
    private double balance = -1;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up packets rate.", false);
    }

    @Override
    public void handle(Packet packet, long time) {

        if(packet.isFlying()) {
            long diff = time - lastFlying;
            balance += 50 - diff;

            if (data.getCollisionProcessor().isTeleporting() ||
                    (data.getJoined() - System.currentTimeMillis()) <= 1000L) balance -= 50.0D;

            debug("balance=" + balance);



            if (balance > 50) {
                if(++buffer >= 1.25) {
                    balance -= 50.0D;
                    fail("balance=" + balance);
                }
            } else if(buffer > 0) buffer -= 0.0125D;


            this.lastFlying = time;


        }

    }
}

package dev.arctic.anticheat.check.impl.player.timer;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.packet.Packet;

public class TimerA extends Check {

    private long lastFlying = System.currentTimeMillis();
    private double balance = -125D;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up packets rate.", false);
    }

    @Override
    public void handle(Packet packet, long time) {

        if(packet.isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final long diff = time - lastFlying;
            balance += 50 - diff;

            if (collisionProcessor.isTeleporting() || collisionProcessor.isNearPiston() ||
                    collisionProcessor.isLastNearPiston()) balance -= 60.0D;

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

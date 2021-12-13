package arctic.ac.check.impl.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;

public class TimerA extends Check {

    private long lastFlying;
    private double balance;


    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up packets rate.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (isFlyingPacket(packetType)) {

            long systemTime = System.currentTimeMillis();
            long lastTimeRate = this.lastFlying != 0 ? this.lastFlying : systemTime - 50;
            this.lastFlying = systemTime;
            balance += 48.25; //don't ask.
            balance -= (systemTime - lastTimeRate);

            if (Math.abs(balance) > 125 || (System.currentTimeMillis() - data.getJoin()) < 500L) balance = -50.0D;

            debug("balance=" + balance + " buffer=" + buffer);

            if(balance > 30) {
                buffer += balance;

                if(buffer >= 525) {
                    fail("balance=" + balance);
                }
            } else if(buffer > 0) buffer -= 25;






        } else if (packetType == PacketType.OUT_POSITION) {
            balance -= 50.0D;

        }

    }

}

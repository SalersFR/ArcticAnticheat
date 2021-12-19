package arctic.ac.check.impl.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;

public class TimerA extends Check {

    private long lastFlying = System.currentTimeMillis();
    private double balance = -1;


    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is speeding up packets rate.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (isFlyingPacket(packetType)) {

            long diff = time - lastFlying;
            balance += 50 - diff;

            if (data.getMovementProcessor().isTeleported()) balance -= 50.0D;

            debug("balance=" + balance + " tpTicks=" + data.getMovementProcessor().getTeleportTicks());

            if (balance < -125) balance += 10;

            if (balance < -3000) balance = -120;

            if (balance > 50) {
                if (++buffer > 12)
                    balance -= 50.0D;
                fail("balance=" + balance);
            } else if(buffer > 0) buffer -= 0.1D;


            this.lastFlying = time;


        }

    }

}

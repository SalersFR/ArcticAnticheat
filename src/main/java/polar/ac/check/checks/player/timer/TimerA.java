package polar.ac.check.checks.player.timer;

import com.avaje.ebeaninternal.server.cluster.PacketTransactionEvent;
import com.comphenix.protocol.PacketType;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.FlyingEvent;
import polar.ac.event.client.PacketEvent;

public class TimerA extends Check {
    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", true);
    }

    private long lastTickTime;
    private double balance;


    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent){
            if (data.getInteractData().getLastHitPacket() != 0){
                if (getMillis(data.getInteractData().getLastHitPacket()) < 110L) return;
            }
            long systemTime = System.currentTimeMillis();
            long lastTimeRate = this.lastTickTime != 0 ? this.lastTickTime : systemTime - 50;
            this.lastTickTime = systemTime;
            balance += 50.0;
            balance -= (systemTime - lastTimeRate);

            if (balance >= 30.0){
                if (++buffer > 6){
                    fail("balance=" + balance);
                    balance = 0.0D;
                }
            }else {
                buffer -= (buffer > 0) ? 2 : 0;
            }

            debug("balance=" + balance);
        }else if (e instanceof PacketEvent){
            if (((PacketEvent) e).getPacketType().equals(PacketType.Play.Server.POSITION)){
                balance -= 50.0D;
            }
        }
    }

    private long getMillis(long val){
        return System.currentTimeMillis() - val;
    }
}

package arctic.ac.check.impl.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInUseEntity;

public class KillAuraA extends Check {

    private long lastFlying, lastFlyingDelay;
    private double average = 50;
    private int hits;

    public KillAuraA(PlayerData data) {
        super(data, "KillAura", "A", "combat.killaura.a", "Checks for invalid delay between attack and flying packet", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_USE_ENTITY) {
            final WrappedInUseEntity wrapped = new WrappedInUseEntity(packet);
            if (wrapped.getUseAction() == WrappedInUseEntity.UseEntityAction.ATTACK) {
                final long delta = Math.abs(System.currentTimeMillis() - this.lastFlying);

                average = ((average * 14) + delta) / 15;

                debug("elapsed=" + delta + " current=" + System.currentTimeMillis() + " last=" + lastFlying);

                if (lastFlyingDelay > 10L && lastFlyingDelay < 90L) {
                    if (average < 5 && hits++ > 10) {
                        fail("delta=" + average);
                        average = 5;
                    }
                }


            }
        } else if (isFlyingPacket(packetType)) {
            this.lastFlyingDelay = System.currentTimeMillis() - lastFlying;
            this.lastFlying = System.currentTimeMillis();
        }

    }
}

package arctic.ac.data.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.event.server.ServerVelocityEvent;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@Getter
@Setter
public class VelocityData {

    public Vector velocity = new Vector();
    public double velocityY, originalVelocityY, originalVelocityX, originalVelocityZ, velocityX, velocityZ;
    public int velocityTicks = 1000;
    private short velocityID = 10000;


    private PlayerData data;

    public VelocityData(PlayerData data) {
        this.data = data;
    }

    public void handleFlying() {
        if (velocityTicks > 0) {
            if (velocityY > 0) {
                velocityY -= 0.08D;
                velocityY *= 0.98F;

                if (getData().getInteractData().isSprinting()) {
                    velocityX *= 0.6F;
                    velocityZ *= 0.6F;
                }

                velocityX *= 0.91F;
                velocityZ *= 0.91F;

            } else velocityY = 0;
        }

        if (velocityY < 0.005) {
            velocityY = 0;
        }

        if (velocityX < 0.005)
            velocityX = 0;

        if (velocityZ < 0.005)
            velocityZ = 0;

        ++velocityTicks;
    }

    public void handleTransaction(WrappedPacketInTransaction wrapper) {
        if (wrapper.getActionNumber() == velocityID) {
            velocityY = originalVelocityY = velocity.getY();
            velocityX = originalVelocityX = velocity.getX();
            velocityZ = originalVelocityZ = velocity.getZ();
            velocityTicks = 0;

        }
    }


    public void handleVelocity(ServerVelocityEvent e) {
        if (e.getEntityID() == data.getPlayer().getEntityId()) {
            double x = Math.abs(e.getX());
            double y = e.getY();
            double z = Math.abs(e.getZ());

            velocity = new Vector(x, y, z);
            velocityID--;

            PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(),
                    new WrappedPacketOutTransaction(0, velocityID, false));

            if (velocityID <= -1) {
                velocityID = 900;
            }
        }
    }
}

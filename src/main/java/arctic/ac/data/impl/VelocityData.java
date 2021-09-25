package arctic.ac.data.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.event.server.ServerVelocityEvent;
import arctic.ac.utils.WorldUtils;
import com.comphenix.packetwrapper.WrapperPlayClientTransaction;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
public class VelocityData {

    public Vector velocity = new Vector();
    public double velocityY, originalVelocityY;
    public int velocityTicks = 1000;
    private short velocityID = 10000;

    private PlayerData data;

    public VelocityData(PlayerData data) {
        this.data = data;
    }

    public void handleFlying() {
        if(velocityTicks > 0) {
            if(velocityY > 0) {
                velocityY -= 0.08D;
                velocityY *= 0.98F;
            } else velocityY = 0;
        }

        if(velocityY < 0.005) {
            velocityY = 0;
        }

        ++velocityTicks;
    }

    public void handleTransaction(WrapperPlayClientTransaction wrapper) {
        if(wrapper.getActionNumber() == velocityID) {
            velocityY = originalVelocityY = velocity.getY();
            velocityTicks = 0;
        }
    }

    public void handleVelocity(ServerVelocityEvent e) {
        if(e.getEntityID() == data.getPlayer().getEntityId()) {
            double x = Math.abs(e.getX());
            double y = e.getY();
            double z = Math.abs(e.getZ());

            velocity = new Vector(x, y, z);
            velocityID--;

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.TRANSACTION);
            packet.getBooleans().write(0, false);
            packet.getShorts().write(0, velocityID);
            packet.getIntegers().write(0, 0);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), packet);
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }

            if(velocityID <= 200) {
                velocityID = 10000;
            }
        }
    }

}

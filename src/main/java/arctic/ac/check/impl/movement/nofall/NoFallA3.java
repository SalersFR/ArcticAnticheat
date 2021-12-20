package arctic.ac.check.impl.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;
import eu.salers.salty.packet.wrappers.play.in.impl.WrappedInFlying;
import org.bukkit.Location;
import org.bukkit.World;

public class NoFallA3 extends Check {

    public NoFallA3(PlayerData data) {
        super(data, "NoFall", "A3", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(isFlyingPacket(packetType)) {
            WrappedInFlying wrapper = new WrappedInFlying(packet);
            World world = data.getPlayer().getWorld();
            Location pos = new Location(world,wrapper.getX() ,wrapper.getY() ,wrapper.getZ() );

            boolean onGround = wrapper.isOnGround();
            boolean serverSideGround = data.getWorldTracker().onGround(pos,0.6,0,0.51,world);


            if (onGround && !serverSideGround) {
                data.getPlayer().sendMessage("GroundSpoofing");
            }
        }

    }
}

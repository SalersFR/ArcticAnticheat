package arctic.ac.data.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.utils.ALocation;
import com.comphenix.packetwrapper.WrapperPlayClientFlying;
import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PositionData {

    private PacketType lastPacket;
    private long lastFlying;
    private Player player;
    private boolean teleported;
    private long lastTeleport;
    private PlayerData data;

    private ALocation aLocation;

    public PositionData(PlayerData data) {
        this.player = data.getPlayer();
        this.data = data;
    }

    public void handle(final PacketEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.POSITION) {
            final WrapperPlayClientPosition wrapper = new WrapperPlayClientPosition(event.getPacket());
            aLocation = new ALocation(wrapper.getX(),wrapper.getY(),wrapper.getZ());

        } else if(event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
            final WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(event.getPacket());
            aLocation = new ALocation(wrapper.getX(),wrapper.getY(),wrapper.getZ());

        }



    }
}

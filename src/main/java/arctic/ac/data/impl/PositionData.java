package arctic.ac.data.impl;

import arctic.ac.data.PlayerData;
import com.comphenix.protocol.PacketType;
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

    private long lastTeleported;
    private boolean teleporting;

    public PositionData(PlayerData data) {
        this.player = data.getPlayer();
        this.data = data;
    }
}

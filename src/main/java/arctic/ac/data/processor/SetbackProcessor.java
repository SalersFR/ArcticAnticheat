package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.ALocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class SetbackProcessor {

    private final PlayerData data;

    private ALocation lastGroundLocation;

    private int ticksSince;

    public void handle(final MoveEvent event) {
        if (event.isGround()) {
            this.lastGroundLocation = event.getTo();
        }
        this.ticksSince++;


    }

    public void setback() {
        final Player player = data.getPlayer();
        player.teleport(new Location(player.getWorld(),lastGroundLocation.getX(),lastGroundLocation.getY() + 0.001D,lastGroundLocation.getZ()));
        this.ticksSince = 0;

    }
}

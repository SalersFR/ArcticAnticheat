package dev.arctic.anticheat.check.impl.player.scaffold;

import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.PlayerUtils;
import dev.arctic.anticheat.utilities.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

public class ScaffoldE extends Check {
    public ScaffoldE(PlayerData data) {
        super(data, "Scaffold", "E", "player.scaffold.e", "Checks if player is sprinting while bridging.", true);
    }

    public boolean isBridging() {
        return data.getPlayer().getLocation().clone().subtract(0, 2, 0).getBlock().getType() == Material.AIR && data.getPlayer().getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid() && data.getRotationProcessor().getPitch() > 70 && data.getRotationProcessor().getPitch() < 81;
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isBlockPlace()) {
            WrapperPlayClientBlockPlace wrapperPlayClientBlockPlace = new WrapperPlayClientBlockPlace(packet);
            if(wrapperPlayClientBlockPlace.getFace() == 255) {
                return;
            }
            EnumWrappers.Direction dir = wrapperPlayClientBlockPlace.getDirection();
            if(dir == EnumWrappers.Direction.DOWN || dir == EnumWrappers.Direction.UP) {
                return;
            }
            // 216 normal
            // 303 speed 1
            // 346 speed 2
            double limit = 0.216;
            limit = data.getPlayer().hasPotionEffect(PotionEffectType.SPEED) ? limit + (PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED) * 0.6F) : limit;

            final boolean invalid = data.getMovementProcessor().getDeltaXZ() > limit && isBridging();
            if(invalid) {
                fail("DeltaXZ: " + data.getMovementProcessor().getDeltaXZ());
            }
        }
    }
}

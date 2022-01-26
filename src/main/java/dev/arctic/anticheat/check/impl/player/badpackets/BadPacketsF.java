package dev.arctic.anticheat.check.impl.player.badpackets;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

/**
 * @author xWand
 */
public class BadPacketsF extends Check {

    private boolean sentSprint;

    public BadPacketsF(PlayerData data) {
        super(data, "BadPackets", "F", "player.badpackets.f", "Checks if player is sprinting and eating.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isEntityAction()) {
            final WrapperPlayClientEntityAction wrapped = new WrapperPlayClientEntityAction(packet);

            if (wrapped.getAction().toString().toLowerCase().contains("sprint")) {
                sentSprint = true;
            }
        } else if (packet.isFlying()) {
            sentSprint = false;
        } else if (packet.isUseItem()) {

        }
    }

    /**
    private boolean isFoodStuffs(ItemStack i) {
        List<Material> foods = (List<Material>) Arrays.asList(
                Material.COOKED_BEEF,
                Material.COOKED_CHICKEN,
                Material.COOKED_FISH,
                Material.COOKED_MUTTON,
                Material.COOKED_RABBIT,
                Material.APPLE,
                Material.GOLDEN_APPLE,
                Material.GOLDEN_CARROT,
                Material.RAW_BEEF,
                Material.RABBIT,
                Material.RABBIT_FOOT,
                Material.BAKED_POTATO,
                Material.POTATO);

        return foods.stream().anyMatch(i.getType());
    }
     **/
}

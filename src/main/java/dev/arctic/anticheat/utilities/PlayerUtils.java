package dev.arctic.anticheat.utilities;

import dev.arctic.anticheat.data.PlayerData;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;

@UtilityClass
public class PlayerUtils {

    public int getPotionLevel(final Player player, final PotionEffectType pet) {
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equalsIgnoreCase(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }

    public final int getPing(PlayerData data) {
        Object entityPlayer = " ";
        int ping = -1;
        try {
            entityPlayer = data.getPlayer().getClass().getMethod("getHandle").invoke(data.getPlayer());
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NoSuchFieldException e) {

        }
        return ping;
    }
}

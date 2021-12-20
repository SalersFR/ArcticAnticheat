package arctic.ac.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
@Data
public class ArcticPotionEffect {

    private final int level;
    private final PotionEffectType type;
}

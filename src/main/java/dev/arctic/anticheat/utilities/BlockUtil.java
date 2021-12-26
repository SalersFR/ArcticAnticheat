package dev.arctic.anticheat.utilities;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;

@UtilityClass
public class BlockUtil {

    public float getBlockFriction(@NonNull Block bBlock) {
        String block = bBlock.getType().name();
        return block.equals("blue ice") ? 0.989f : block.contains("ice") ? 0.98f : block.equals("slime") ? 0.8f : 0.6f;
    }
}

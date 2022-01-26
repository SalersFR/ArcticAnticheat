package dev.arctic.anticheat.manager;

import com.comphenix.protocol.wrappers.Pair;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

//CREDITS TO MEDUSA
public class TicksManager extends BukkitRunnable {

    @Getter
    private int totalTicks;

    @Override
    public void run() {
        totalTicks++;
        for (final PlayerData playerDatas : Arctic.getInstance().getPlayerDataManager().getAllData()) {
            final LivingEntity target = playerDatas.getCombatProcessor().getTarget();
            final LivingEntity lastTarget = playerDatas.getCombatProcessor().getLastTarget();

            if (target != null && lastTarget != null) {
                if (target != lastTarget) playerDatas.getTargetLocations().clear();

                final Vector location = target.getLocation().toVector();
                playerDatas.getTransactionProcessor().todoTransaction(()
                        -> playerDatas.getTargetLocations().add(new Pair<>(location, totalTicks)));



            }
        }
    }

}


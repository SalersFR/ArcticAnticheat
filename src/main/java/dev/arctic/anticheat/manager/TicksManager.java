package dev.arctic.anticheat.manager;

import com.comphenix.protocol.wrappers.Pair;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.data.PlayerData;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

//CREDITS TO MEDUSA
public class TicksManager extends BukkitRunnable {

    @Getter
    private int totalTicks;

    @Override
    public void run() {
        totalTicks++;
        for(final PlayerData playerDatas : Arctic.getInstance().getPlayerDataManager().getAllData()) {
            final Entity target = playerDatas.getCombatProcessor().getTarget();

            if(target != null) {


            }
        }

    }
}

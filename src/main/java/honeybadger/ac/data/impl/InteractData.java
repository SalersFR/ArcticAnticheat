package honeybadger.ac.data.impl;

import honeybadger.ac.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

public class InteractData {

    @Getter
    private boolean isDigging;
    @Getter
    private boolean isPlacing;
    private Player player;

    public InteractData(PlayerData data) {
        this.player = data.getBukkitPlayerFromUUID();
    }

    public void setDigging(boolean b) {
        this.isDigging = b;
    }

    public void setPlacing(boolean b) {
        this.isPlacing = b;
    }
}

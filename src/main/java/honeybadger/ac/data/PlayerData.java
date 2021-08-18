package honeybadger.ac.data;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.impl.CheckManager;
import honeybadger.ac.utils.HLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
public class PlayerData {

    private final CheckManager checkManager;
    private final Player player;

    @Setter
    private HLocation location;

    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.player = player;
    }

    public List<Check> getChecks() {
        return this.checkManager.getChecks();
    }

    /**
     * Getting a bukkit player from a uuid
     *
     * @return the player reliated to the uuid
     */
    public Player getBukkitPlayerFromUUID() {
        return this.player;
    }
}

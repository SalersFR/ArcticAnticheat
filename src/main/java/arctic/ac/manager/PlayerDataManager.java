package arctic.ac.manager;


import arctic.ac.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {


    /**
     * Data cache
     **/
    @Getter
    private final Map<Player, PlayerData> uuidPlayerDataMap = new ConcurrentHashMap<>();

    /**
     * Getting a PlayerData from the cache
     *
     * @param player the uuid for getting a PlayerData
     * @return a PlayerData from the param player
     **/

    public PlayerData getPlayerData(Player player) {
        return this.uuidPlayerDataMap.get(player);
    }

    /**
     * Adding a PlayerData to a cache
     *
     * @param player the player to add in the cache and a new instance of a PlayerData
     */

    public void add(Player player) {
        this.uuidPlayerDataMap.put(player, new PlayerData(player));
    }

    /**
     * Removing a PlayerData from the cache
     *
     * @param player the player to remove in the cache and also remove the PlayerData reliated to the player
     */

    public void remove(Player player) {
        this.uuidPlayerDataMap.remove(player, this.getPlayerData(player));
    }


}

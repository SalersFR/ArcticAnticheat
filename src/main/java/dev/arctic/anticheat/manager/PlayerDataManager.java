package dev.arctic.anticheat.manager;

import dev.arctic.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final Map<Player, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    public PlayerData getPlayerData(final Player player) {
        return playerDataMap.getOrDefault(player, null);
    }

    public void add(final Player player) {
        final PlayerData data = new PlayerData(player);
        data.setJoined(System.currentTimeMillis());
        playerDataMap.put(player, data);
    }

    public boolean has(final Player player) {
        return this.playerDataMap.containsKey(player);
    }

    public void remove(final Player player) {
        playerDataMap.remove(player);
    }

    public Collection<PlayerData> getAllData() {
        return playerDataMap.values();
    }
}

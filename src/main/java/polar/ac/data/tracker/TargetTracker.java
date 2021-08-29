package polar.ac.data.tracker;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;
import polar.ac.data.PlayerData;
import polar.ac.utils.ConcurrentList;

@Getter
@RequiredArgsConstructor
public class TargetTracker {

    private final PlayerData data;

    public void handleSpawn(WrapperPlayServerNamedEntitySpawn packet) {

    }

    @Getter
    public static class TrackedEntity {
        private ConcurrentList<Vector> possibleLocations = new ConcurrentList<>();


    }
}

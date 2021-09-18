package arctic.ac.data.tracker;

import arctic.ac.data.PlayerData;
import arctic.ac.utils.ConcurrentList;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

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

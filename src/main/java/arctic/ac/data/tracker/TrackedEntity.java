package arctic.ac.data.tracker;

import arctic.ac.utils.ConcurrentList;
import lombok.Getter;

@Getter
public class TrackedEntity {

    private ConcurrentList<EntityLocation> possibleLocations = new ConcurrentList<>();

    public void interpolate() {
        possibleLocations.forEach(location -> location.interpolate());
    }

    public void onUpdate(EntityUpdate update) {
        if (update.isConfirmed()) {

        }
    }
}

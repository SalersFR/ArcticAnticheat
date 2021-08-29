package arctic.ac.data.tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

@Getter
@RequiredArgsConstructor
public class EntityUpdate {

    private final short transId;
    private final int entityId;
    private final EntityUpdateType updateType;
    private final Vector movement;

    @Getter
    private boolean responded = false;
}

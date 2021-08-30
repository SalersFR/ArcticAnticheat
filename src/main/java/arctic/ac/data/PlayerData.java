package arctic.ac.data;

import arctic.ac.check.Check;
import arctic.ac.data.impl.InteractData;
import arctic.ac.data.impl.PositionData;
import arctic.ac.data.tracker.TargetTracker;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.ARotation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class PlayerData {

    private final CheckManager checkManager;
    private final InteractData interactData;
    private final PositionData posData;
    private final TargetTracker targetTracker;
    private final Player player;


    @Setter
    private ALocation location;

    @Setter
    private ARotation rotation;

    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.interactData = new InteractData(this);
        this.posData = new PositionData(this);
        this.targetTracker = new TargetTracker(this);
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

    public InteractData getInteractionData() {
        return this.interactData;
    }
}

package honeybadger.ac.data;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.impl.CheckManager;
import honeybadger.ac.data.impl.InteractData;
import honeybadger.ac.data.impl.TargetTracker;
import honeybadger.ac.utils.HLocation;
import honeybadger.ac.utils.HRotation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class PlayerData {

    private final CheckManager checkManager;
    private final InteractData interactData;
    private final TargetTracker targetTracker;
    private final Player player;


    @Setter
    private HLocation location;

    @Setter
    private HRotation rotation;

    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.interactData = new InteractData(this);
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

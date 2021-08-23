package polar.ac.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.entity.Player;
import polar.ac.check.Check;
import polar.ac.data.impl.*;
import polar.ac.prediction.PredictionManager;
import polar.ac.utils.PLocation;
import polar.ac.utils.PRotation;

import java.util.List;

@Getter
public class PlayerData {

    private final CheckManager checkManager;
    private final InteractData interactData;
    private final PositionData posData;
    private final TargetTracker targetTracker;
    private final PredictionData predictionData;
    private final PredictionManager predictionManager;
    private final Player player;



    @Setter
    private PLocation location;

    @Setter
    private PRotation rotation;

    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.interactData = new InteractData(this);
        this.posData = new PositionData(this);
        this.targetTracker = new TargetTracker(this);
        this.predictionData = new PredictionData(this);
        this.predictionManager = new PredictionManager(this);
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

package arctic.ac.data;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.impl.CheckManager;
import arctic.ac.data.impl.InteractData;
import arctic.ac.data.impl.PositionData;
import arctic.ac.data.impl.VelocityData;
import arctic.ac.data.processor.CinematicProcessor;
import arctic.ac.data.processor.PacketProcessor;
import arctic.ac.data.tracker.TargetTracker;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.ARotation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerData {

    private final CheckManager checkManager;
    private final InteractData interactData;
    private final PositionData posData;
    private final VelocityData velocityData;
    private final TargetTracker targetTracker;
    private final PacketProcessor packetProcessor;
    private final CinematicProcessor cinematicProcessor;
    private final Player player;
    private final List<ALocation> pastEntityLocations;

    @Setter
    private LivingEntity target;


    @Setter
    private ALocation location;

    @Setter
    private ARotation rotation;

    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.interactData = new InteractData(this);
        this.posData = new PositionData(this);
        this.velocityData = new VelocityData(this);
        this.targetTracker = new TargetTracker(this);
        this.packetProcessor = new PacketProcessor(this);
        this.cinematicProcessor = new CinematicProcessor(this);
        this.pastEntityLocations = new ArrayList<>();
        this.player = player;

        new BukkitRunnable() {

            @Override
            public void run() {
                if(target != null) {
                    final Vector eye = target.getEyeLocation().toVector();
                    pastEntityLocations.add(new ALocation(eye.getX(),eye.getY(),eye.getZ()));
                    if(pastEntityLocations.size() >= 20) {
                        pastEntityLocations.clear();
                    }
                }
            }
        }.runTaskTimer(Arctic.INSTANCE,0L,1L);




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

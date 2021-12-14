package arctic.ac.data;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.processor.impl.*;
import arctic.ac.manager.CheckManager;
import arctic.ac.utils.ALocation;
import arctic.ac.utils.APosition;
import arctic.ac.utils.ARotation;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerData {

    private final CheckManager checkManager;

    private final Player player;
    private final List<ALocation> pastEntityLocations;

    private final RotationProcessor rotationProcessor;
    private final MovementProcessor movementProcessor;
    private final CollisionProcessor collisionProcessor;
    private final ClickProcessor clickProcessor;
    private final CombatProcessor combatProcessor;

    private long join;


    @Setter
    private ALocation location;


    public PlayerData(Player player) {
        this.checkManager = new CheckManager(this);
        this.pastEntityLocations = new ArrayList<>();
        this.player = player;

        this.rotationProcessor = new RotationProcessor(this);
        this.movementProcessor = new MovementProcessor(this);
        this.collisionProcessor = new CollisionProcessor(this);
        this.clickProcessor = new ClickProcessor(this);
        this.combatProcessor = new CombatProcessor(this);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (combatProcessor.getTarget() != null) {
                    final Vector eye = combatProcessor.getTarget().getLocation().toVector();
                    pastEntityLocations.add(new ALocation(eye.getX(), eye.getY(), eye.getZ()));
                    if (pastEntityLocations.size() >= 20) {
                        pastEntityLocations.clear();
                    }
                }
            }
        }.runTaskTimer(Arctic.INSTANCE, 0L, 1L);


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

    public void handleReceive(final SaltyPacketInReceiveEvent event) {

        rotationProcessor.handleIn(event);
        movementProcessor.handleIn(event);
        collisionProcessor.handleIn(event);
        clickProcessor.handleIn(event);
        combatProcessor.handleIn(event);

        //check handling
        final boolean bypass = getPlayer().hasPermission(Arctic.INSTANCE.getConfig().getString("bypass-permission")) &&
                Arctic.INSTANCE.getConfig().getBoolean("bypass-enabled");

        final boolean exempt = getPlayer().getGameMode() == GameMode.CREATIVE
                || getPlayer().getAllowFlight()
                || getPlayer().getGameMode() == GameMode.SPECTATOR
                || bypass;

        for (Check checks : getChecks()) {
            if (checks.isEnabled() && !exempt) {
                Arctic.INSTANCE.getChecksThread().execute(() -> checks.handle(event.getPacket(), event.getPacketType()));

            }
        }
    }



    public void handleSending(final SaltyPacketOutSendEvent event) {

        movementProcessor.handleOut(event);

        //check handling
        final boolean bypass = getPlayer().hasPermission(Arctic.INSTANCE.getConfig().getString("bypass-permission")) &&
                Arctic.INSTANCE.getConfig().getBoolean("bypass-enabled");

        final boolean exempt = getPlayer().getGameMode() == GameMode.CREATIVE
                || getPlayer().getAllowFlight()
                || getPlayer().getGameMode() == GameMode.SPECTATOR
                || bypass;

        for (Check checks : getChecks()) {
            if (checks.isEnabled() && !exempt) {
                Arctic.INSTANCE.getChecksThread().execute(() -> checks.handle(event.getPacket(), event.getPacketType()));

            }
        }
    }


}

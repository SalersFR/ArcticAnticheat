package dev.arctic.anticheat.data;

import com.comphenix.protocol.wrappers.Pair;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.processors.impl.*;
import dev.arctic.anticheat.manager.CheckManager;
import dev.arctic.anticheat.threading.ArcticThread;
import dev.arctic.anticheat.packet.event.PacketEvent;
import dev.arctic.anticheat.utilities.EvictingList;
import lombok.Data;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


@Data
public class PlayerData {

    private final Player player;
    private final CheckManager checkManager;

    private final CombatProcessor combatProcessor;
    private final ActionProcessor actionProcessor;
    private final TransactionProcessor transactionProcessor;
    private final ClickProcessor clickProcessor;
    private final ConnectionProcessor connectionProcessor;
    private final CollisionProcessor collisionProcessor;
    private final RotationProcessor rotationProcessor;
    private final MovementProcessor movementProcessor;
    private final VelocityProcessor velocityProcessor;
    private final CancelProcessor cancelProcessor;
    private final GhostBlockProcessor ghostblockProcessor;

    private ArcticThread thread;


    private final EvictingList<Pair<Vector, Integer>> targetLocations
            = new EvictingList<>(40, false);
    private long joined;



    public PlayerData(final Player player) {

        //associating a player to playerData
        this.player = player;

        //init checkManager
        this.checkManager = new CheckManager(this);

        //init processors
        this.rotationProcessor = new RotationProcessor(this);
        this.movementProcessor = new MovementProcessor(this);
        this.connectionProcessor = new ConnectionProcessor(this);
        this.collisionProcessor = new CollisionProcessor(this);
        this.clickProcessor = new ClickProcessor(this);
        this.transactionProcessor = new TransactionProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.combatProcessor = new CombatProcessor(this);
        this.velocityProcessor = new VelocityProcessor(this);
        this.cancelProcessor = new CancelProcessor(this);
        this.ghostblockProcessor = new GhostBlockProcessor(this);

    }

    public void handle(PacketEvent event) {


        if (event.getPacket().isReceiving()) {
            movementProcessor.handleReceive(event);
            rotationProcessor.handleReceive(event);
            collisionProcessor.handleReceive(event);
            ghostblockProcessor.handleReceive(event);
            connectionProcessor.handleSending(event);
            clickProcessor.handleReceive(event);
            actionProcessor.handleReceive(event);
            combatProcessor.handleReceive(event);
            velocityProcessor.handleReceive(event);
            transactionProcessor.handleReceive(event);
            cancelProcessor.handleReceive(event);
        } else if(event.getPacket().isSending()) {
            collisionProcessor.handleSending(event);
            connectionProcessor.handleSending(event);
            velocityProcessor.handleSending(event);
        }

        final boolean bypass = getPlayer().hasPermission(Arctic.getInstance().getPlugin().getConfig().getString("bypass-permission")) &&
                Arctic.getInstance().getPlugin().getConfig().getBoolean("bypass-enabled");

        final boolean exempt = getPlayer().getGameMode() == GameMode.CREATIVE
                || getPlayer().getAllowFlight()
                || getPlayer().getGameMode() == GameMode.SPECTATOR
                || bypass;

        for (Check check : checkManager.getChecks()) {
            if (check.isEnabled() && !exempt) {
                check.handle(event.getPacket(), event.getTime());
            }
        }
    }


}

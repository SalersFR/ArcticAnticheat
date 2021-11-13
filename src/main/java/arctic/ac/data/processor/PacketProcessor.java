package arctic.ac.data.processor;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.client.*;
import arctic.ac.event.server.ServerPositionEvent;
import arctic.ac.event.server.ServerVelocityEvent;
import arctic.ac.utils.AEntity;
import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

@RequiredArgsConstructor
public class PacketProcessor {

    private final PlayerData data;

    public void handleReceive(PacketEvent event) {
        if (data == null) return;

        data.getPosData().setLastPacket(event.getPacketType());

        final boolean bypass = data.getPlayer().hasPermission(Arctic.INSTANCE.getConfig().getString("bypass-permission")) &&
                Arctic.INSTANCE.getConfig().getBoolean("bypass-enabled");

        final boolean exempt = data.getPlayer().getGameMode() == GameMode.CREATIVE
                || data.getPlayer().getAllowFlight()
                || data.getPlayer().getGameMode() == GameMode.SPECTATOR
                || data.getInteractionData().isTeleported()
                || bypass
                || data.getInteractData().getTicksSinceTeleport() < 10
                || data.getSetbackProcessor().getTicksSince() < 4;

        data.getNetworkProcessor().handleIn(event);

        for (Check checks : data.getChecks()) {
            if (checks.isEnabled() && !exempt) {
                checks.handle(new arctic.ac.event.client.PacketEvent(event));
            }
        }


        if (event.getPacketType() == PacketType.Play.Client.LOOK) {
            final WrapperPlayClientLook wrapper = new WrapperPlayClientLook(event.getPacket());

            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();

            data.getCinematicProcessor().process(rotationEvent);

            for(AEntity entities : data.getEntityTracker().getTrackedEntities()) {
                if(entities.getId() == data.getTarget().getEntityId())
                    entities.interpolate();
            }

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(rotationEvent);
                    checks.handle(flyingEvent);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.FLYING) {

            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getVelocityData().handleFlying();

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(flyingEvent);
                }
            }

            for(AEntity entities : data.getEntityTracker().getTrackedEntities()) {
                if(entities.getId() == data.getTarget().getEntityId())
                    entities.interpolate();
            }

        } else if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
            final WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(event.getPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getYaw(), wrapper.getPitch(),wrapper.getOnGround());
            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getCinematicProcessor().process(rotationEvent);

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();
            data.getSetbackProcessor().handle(moveEvent);

            for(AEntity entities : data.getEntityTracker().getTrackedEntities()) {
                if(entities.getId() == data.getTarget().getEntityId())
                    entities.interpolate();
            }

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(moveEvent);
                    checks.handle(rotationEvent);
                    checks.handle(flyingEvent);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.POSITION) {
            final WrapperPlayClientPosition wrapper = new WrapperPlayClientPosition(event.getPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(),0,0, wrapper.getOnGround());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();
            data.getSetbackProcessor().handle(moveEvent);

            for(AEntity entities : data.getEntityTracker().getTrackedEntities()) {
                if(entities.getId() == data.getTarget().getEntityId())
                    entities.interpolate();
            }

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(moveEvent);
                    checks.handle(flyingEvent);
                }


            }
        } else if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            final WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());

            final UseEntityEvent useEntityEvent = new UseEntityEvent(wrapper, data.getBukkitPlayerFromUUID().getWorld());

            if (wrapper.getTarget(event) != null) {
                data.getInteractData().handleUseEntity(wrapper);
            }
            data.getInteractionData().setLastHitPacket(System.currentTimeMillis());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(useEntityEvent);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
            final WrapperPlayClientArmAnimation wrapper = new WrapperPlayClientArmAnimation(event.getPacket());

            final ArmAnimationEvent armAnimationEvent = new ArmAnimationEvent(data, wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(armAnimationEvent);
            }


            // PLAYER DATA
        } else if (event.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {

            final WrapperPlayClientBlockDig wrapper = new WrapperPlayClientBlockDig(event.getPacket());
            final boolean isSolid = wrapper.getLocation().toLocation(event.getPlayer().getWorld()).getBlock().getType().isSolid();

            if (isSolid && wrapper.getStatus() != EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                data.getInteractionData().handleDigging();
            }


        } else if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {

            final WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event.getPacket());

            data.getInteractData().handleActionPacket(wrapper);

            final EntityActionEvent entityActionEvent = new EntityActionEvent(wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(entityActionEvent);
                }
            }


        } else if (event.getPacketType() == PacketType.Play.Client.TRANSACTION) {

            final WrapperPlayClientTransaction wrapper = new WrapperPlayClientTransaction(event.getPacket());
            final TransactionConfirmEvent transactionConfirmEvent = new TransactionConfirmEvent(wrapper);

            data.getVelocityData().handleTransaction(wrapper);

            for(AEntity entities : data.getEntityTracker().getTrackedEntities()) {
                if(entities.getId() == data.getTarget().getEntityId() && (wrapper.getActionNumber() - entities.getTransactionID()) < 2)
                    entities.handleTransaction();
            }


            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(transactionConfirmEvent);
                }
            }
        }

    }

    public void handleSending(PacketEvent event) {

        if (data == null) return;

        final boolean bypass = data.getPlayer().hasPermission(Arctic.INSTANCE.getConfig().getString("bypass-permission")) &&
                Arctic.INSTANCE.getConfig().getBoolean("bypass-enabled");

        final boolean exempt = data.getPlayer().getGameMode() == GameMode.CREATIVE
                || data.getPlayer().getAllowFlight()
                || data.getPlayer().getGameMode() == GameMode.SPECTATOR
                || data.getInteractionData().isTeleported()
                || bypass
                || data.getInteractData().getTicksSinceTeleport() < 10
                || data.getSetbackProcessor().getTicksSince() < 4;

        data.getNetworkProcessor().handleOut(event);


        for (Check checks : data.getChecks()) {
            if (checks.isEnabled() && !exempt) {
                checks.handle(new arctic.ac.event.client.PacketEvent(event));
            }
        }
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            final WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity(event.getPacket());

            final ServerVelocityEvent serverVelocityEvent = new ServerVelocityEvent(wrapper);

            data.getVelocityData().handleVelocity(serverVelocityEvent);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(serverVelocityEvent);
            }
        } else if (event.getPacketType() == PacketType.Play.Server.POSITION) {

            final WrapperPlayServerPosition wrapper = new WrapperPlayServerPosition(event.getPacket());

            final ServerPositionEvent serverPositionEvent = new ServerPositionEvent(wrapper);


            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(serverPositionEvent);
            }


        } else if (event.getPacketType().equals(PacketType.Play.Server.REL_ENTITY_MOVE)) {
            final WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove(event.getPacket());

            data.getEntityTracker().handleRelMove(wrapper);

        } else if (event.getPacketType().equals(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK)) {
            final WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook(event.getPacket());

            data.getEntityTracker().handleRelMoveLook(wrapper);

        } else if(event.getPacketType().equals(PacketType.Play.Server.ENTITY_TELEPORT)) {
            final WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event.getPacket());

            data.getEntityTracker().handleTeleport(wrapper);
        } else if(event.getPacketType().equals(PacketType.Play.Server.NAMED_ENTITY_SPAWN)) {
            final WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn(event.getPacket());

            data.getEntityTracker().handleNamedSpawn(wrapper);
        }
    }
}

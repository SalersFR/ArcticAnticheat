package arctic.ac.data.processor;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.client.*;
import arctic.ac.event.server.ServerPositionEvent;
import arctic.ac.event.server.ServerVelocityEvent;
import arctic.ac.utils.PlayerUtils;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

@RequiredArgsConstructor
public class PacketProcessor {

    private final PlayerData data;

    public void handleReceive(PacketPlayReceiveEvent event) {
        if (data == null) return;

        data.getPosData().setLastPacket(event.getPacketId());

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

        for(Check check : data.getChecks()) {
            if(!exempt && check.isEnabled())
                check.handle(new PacketReceiveEvent(event));
        }


        if (event.getPacketId() == PacketType.Play.Client.LOOK) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());

            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            long ping = PlayerUtils.getPing(data);
            ping = Math.min(1000, ping);
            data.getPosData().setTeleporting(System.currentTimeMillis() - data.getPosData().getLastTeleported() < ping + 200);

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();

            data.getCinematicProcessor().process(rotationEvent);

            if (data.getTarget() != null)
                data.getEntityTracker().interpolate(data.getTarget().getEntityId());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(rotationEvent);
                    checks.handle(flyingEvent);
                }
            }
        } else if (event.getPacketId() == PacketType.Play.Client.FLYING) {

            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getVelocityData().handleFlying();

            if (data.getTarget() != null)
                data.getEntityTracker().interpolate(data.getTarget().getEntityId());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(flyingEvent);
                }
            }

        } else if (event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround());
            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());
            final PositionEvent positionEvent = new PositionEvent(data, wrapper.getX(), wrapper.getY(),
                    wrapper.getZ(), wrapper.getYaw(), wrapper.getPitch(), wrapper.isOnGround());

            if (data.getTarget() != null)
                data.getEntityTracker().interpolate(data.getTarget().getEntityId());

            data.getCinematicProcessor().process(rotationEvent);

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();
            data.getSetbackProcessor().handle(moveEvent);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(moveEvent);
                    checks.handle(rotationEvent);
                    checks.handle(positionEvent);
                    checks.handle(flyingEvent);
                }
            }
        } else if (event.getPacketId() == PacketType.Play.Client.POSITION) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(), 0, 0, wrapper.isOnGround());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            if (data.getTarget() != null)
                data.getEntityTracker().interpolate(data.getTarget().getEntityId());

            data.getInteractData().handleFlying();
            data.getVelocityData().handleFlying();
            data.getSetbackProcessor().handle(moveEvent);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(moveEvent);
                    checks.handle(flyingEvent);
                }


            }
        } else if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(event.getNMSPacket());

            final UseEntityEvent useEntityEvent = new UseEntityEvent(wrapper, data.getBukkitPlayerFromUUID().getWorld());

            if (wrapper.getEntity() != null) {
                data.getInteractData().handleUseEntity(wrapper);
            }
            data.getInteractionData().setLastHitPacket(System.currentTimeMillis());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(useEntityEvent);
            }
        } else if (event.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {

            final ArmAnimationEvent armAnimationEvent = new ArmAnimationEvent();


            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(armAnimationEvent);
            }

            //data.getInteractData().handleArmAnimation();


            // PLAYER DATA
        } else if (event.getPacketId() == PacketType.Play.Client.BLOCK_DIG) {

            final WrappedPacketInBlockDig wrapper = new WrappedPacketInBlockDig(event.getNMSPacket());


            data.getInteractionData().handleDigging();


        } else if (event.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {

            final WrappedPacketInEntityAction wrapper = new WrappedPacketInEntityAction(event.getNMSPacket());
            data.getInteractData().handleActionPacket(wrapper);

            final EntityActionEvent entityActionEvent = new EntityActionEvent(wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(entityActionEvent);
                }
            }


        } else if (event.getPacketId() == PacketType.Play.Client.TRANSACTION) {

            final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(event.getNMSPacket());
            final TransactionConfirmEvent transactionConfirmEvent = new TransactionConfirmEvent(wrapper);

            data.getVelocityData().handleTransaction(wrapper);

            if (data.getTarget() != null)
                data.getEntityTracker().handleTransaction(data.getTarget().getEntityId(), wrapper.getActionNumber());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(transactionConfirmEvent);
                }
            }
        } else if (event.getPacketId() == PacketType.Play.Client.BLOCK_PLACE) {

            final WrappedPacketInBlockPlace wrapper = new WrappedPacketInBlockPlace(event.getNMSPacket());
            final BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(blockPlaceEvent);

            }

        }

    }

    public void handleSending(PacketPlaySendEvent event) {

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


        if (event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
            final WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(event.getNMSPacket());

            final ServerVelocityEvent serverVelocityEvent = new ServerVelocityEvent(wrapper);

            data.getVelocityData().handleVelocity(serverVelocityEvent);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(serverVelocityEvent);
            }
        } else if (event.getPacketId() == PacketType.Play.Server.POSITION) {

            final WrappedPacketOutPosition wrapper = new WrappedPacketOutPosition(event.getNMSPacket());

            final ServerPositionEvent serverPositionEvent = new ServerPositionEvent(wrapper);

            data.getPosData().setLastTeleported(System.currentTimeMillis());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(serverPositionEvent);
            }


        } else if (event.getPacketId() == PacketType.Play.Server.ENTITY_TELEPORT) {
            final WrappedPacketOutEntityTeleport packet = new WrappedPacketOutEntityTeleport(event.getNMSPacket());
            data.getInteractData().handleOutTeleport(packet);
            data.getEntityTracker().teleport(packet.getEntityId(), packet.getPosition().x, packet.getPosition().y, packet.getPosition().z);
        } else if (event.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE) {
            final WrappedPacketOutEntity.WrappedPacketOutRelEntityMove packet = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(event.getNMSPacket());
            data.getEntityTracker().relMove(packet.getEntityId(), packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
        } else if (event.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            final WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook packet = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(event.getNMSPacket());
            data.getEntityTracker().relMove(packet.getEntityId(), packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
        } else if (event.getPacketId() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            final WrappedPacketOutNamedEntitySpawn packet = new WrappedPacketOutNamedEntitySpawn(event.getNMSPacket());
            data.getEntityTracker().addEntity(packet.getEntityId(), packet.getPosition().x, packet.getPosition().y, packet.getPosition().z);
        }


    }
}


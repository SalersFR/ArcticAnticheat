package polar.ac.listener.packet;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import polar.ac.Polar;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.client.*;
import polar.ac.event.server.ServerVelocityEvent;
import polar.ac.prediction.PredictionHandler;

public class PacketHandler {

    public PacketHandler() {

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        for (PacketType types : PacketType.values()) {
            if (types.isSupported()) {
                manager.addPacketListener(new PacketAdapter(Polar.INSTANCE, types) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PlayerData data = Polar.INSTANCE.getDataManager().getPlayerData(event.getPlayer());
                        if(data == null ) return;
                        handleReceive(data, event);
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PlayerData data = Polar.INSTANCE.getDataManager().getPlayerData(event.getPlayer());
                        if(data == null ) return;
                        handleSending(data, event);
                    }
                });


            }
        }
    }

    public void handleReceive(PlayerData data, PacketEvent event) {
        if (data == null) return;

        final boolean exempt = data.getPlayer().getGameMode() == GameMode.CREATIVE
                || data.getPlayer().getAllowFlight()
                || data.getPlayer().getGameMode() == GameMode.SPECTATOR
                || data.getInteractionData().isTeleported();

        for (Check checks : data.getChecks()) {
            if (checks.isEnabled() && !exempt) {
                checks.handle(new polar.ac.event.client.PacketEvent(event.getPacketType()));
            }
        }

        data.getPosData().setLastPacket(event.getPacketType());

        if (event.getPacketType() == PacketType.Play.Client.LOOK) {
            final WrapperPlayClientLook wrapper = new WrapperPlayClientLook(event.getPacket());

            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getInteractData().handleFlying();

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(rotationEvent);
                    checks.handle(flyingEvent);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.FLYING) {

            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt) {
                    checks.handle(flyingEvent);
                }
            }

        } else if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
            final WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(event.getPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getOnGround());
            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getInteractData().handleFlying();

            for(PredictionHandler preds : data.getPredictionManager().getPredictions()) {
                preds.onMove(moveEvent);
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

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getOnGround());
            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            data.getInteractData().handleFlying();

            for(PredictionHandler preds : data.getPredictionManager().getPredictions()) {
                preds.onMove(moveEvent);
            }


            for (Check checks : data.getChecks()) {
                if (checks.isEnabled() && !exempt)
                    checks.handle(moveEvent);
                checks.handle(flyingEvent);


            }
        } else if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            final WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());

            final UseEntityEvent useEntityEvent = new UseEntityEvent(wrapper, data.getBukkitPlayerFromUUID().getWorld());

            if(wrapper.getTarget(event) != null) {
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

            if (isSolid && event.getPacket().getPlayerDigTypes().read(0).equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK)) {
                data.getInteractionData().setDigging(true);
            } else if (event.getPacket().getPlayerDigTypes().read(0).equals(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK)
                    || event.getPacket().getPlayerDigTypes().read(0).equals(EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK)) {
                data.getInteractionData().setDigging(false);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {

            final WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event.getPacket());

            data.getInteractData().handleActionPacket(wrapper);


        }

    }

    public void handleSending(PlayerData data, PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            final WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity(event.getPacket());

            ServerVelocityEvent serverVelocityEvent = new ServerVelocityEvent(wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled())
                    checks.handle(serverVelocityEvent);
            }

        } else if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {

            final WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove(event.getPacket());

            data.getTargetTracker().handleRelMove(wrapper);
        } else if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {

            final WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook(event.getPacket());

            data.getTargetTracker().handleRelMoveLook(wrapper);

        } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {

            final WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving(event.getPacket());

            data.getTargetTracker().handleSpawn(wrapper);
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {

            final WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event.getPacket());

            data.getTargetTracker().handleTeleport(wrapper);
            data.getInteractionData().handleOutTeleport(wrapper);

        } else if (event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {

            final WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn(event.getPacket());

            data.getTargetTracker().handleNamedEntitySpawn(wrapper);
        }
    }
}

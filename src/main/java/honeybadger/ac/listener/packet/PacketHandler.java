package honeybadger.ac.listener.packet;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.HoneyBadger;
import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.client.*;
import honeybadger.ac.event.server.ServerVelocityEvent;

public class PacketHandler {

    public PacketHandler() {

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        for (PacketType types : PacketType.values()) {
            if (types.isSupported()) {
                manager.addPacketListener(new PacketAdapter(HoneyBadger.INSTANCE, types) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        handleReceive(HoneyBadger.INSTANCE.getDataManager().getPlayerData(event.getPlayer()), event);
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        handleSending(HoneyBadger.INSTANCE.getDataManager().getPlayerData(event.getPlayer()), event);
                    }
                });


            }
        }
    }

    public void handleReceive(PlayerData data, PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.LOOK) {
            final WrapperPlayClientLook wrapper = new WrapperPlayClientLook(event.getPacket());

            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled()) {
                    checks.handle(rotationEvent);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.FLYING
                || event.getPacketType() == PacketType.Play.Client.LOOK
                || event.getPacketType() == PacketType.Play.Client.POSITION_LOOK
                || event.getPacketType() == PacketType.Play.Client.POSITION) {

            final WrapperPlayClientFlying wrapper = new WrapperPlayClientFlying(event.getPacket());

            final FlyingEvent flyingEvent = new FlyingEvent(System.currentTimeMillis());

            for(Check checks : data.getChecks()) {
                if(checks.isEnabled()) {
                    checks.handle(flyingEvent);
                }
            }

        } else if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
            final WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(event.getPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ());
            final RotationEvent rotationEvent = new RotationEvent(data, wrapper.getYaw(), wrapper.getPitch());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled()) {
                    checks.handle(moveEvent);
                    checks.handle(rotationEvent);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.POSITION) {
            final WrapperPlayClientPosition wrapper = new WrapperPlayClientPosition(event.getPacket());

            final MoveEvent moveEvent = new MoveEvent(data, wrapper.getX(), wrapper.getY(), wrapper.getZ());


            for (Check checks : data.getChecks()) {
                if (checks.isEnabled())
                    checks.handle(moveEvent);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            final WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());

            final UseEntityEvent useEntityEvent = new UseEntityEvent(wrapper, data.getBukkitPlayerFromUUID().getWorld());

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled())
                    checks.handle(useEntityEvent);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
            final WrapperPlayClientArmAnimation wrapper = new WrapperPlayClientArmAnimation(event.getPacket());

            final ArmAnimationEvent armAnimationEvent = new ArmAnimationEvent(data, wrapper);

            for (Check checks : data.getChecks()) {
                if (checks.isEnabled())
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
        } else if(event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {

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

        }
    }
}

package dev.arctic.anticheat.check.impl.player.badpackets;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class BadPacketsA extends Check {

    private boolean sentSneak, sentSprint;

    public BadPacketsA(PlayerData data) {
        super(data, "BadPackets", "A", "player.badpackets.a", "Checks for invalid sprint/sneak packets.",false);

    }
    @Override
    public void handle(Packet packet, long time) {
        if(packet.isEntityAction()) {
            final WrapperPlayClientEntityAction wrapped = new WrapperPlayClientEntityAction(packet);

            if (wrapped.getAction().toString().toLowerCase().contains("sprint")) {
                sentSprint = true;
            }

            if (wrapped.getAction().toString().toLowerCase().contains("sneak")) {
                sentSneak = true;
            }
        } else if(packet.isFlying()) {
            sentSneak = sentSprint = false;
        } else if(packet.isUseEntity()) {
            if(new WrapperPlayClientUseEntity(packet).getType() == EnumWrappers.EntityUseAction.ATTACK) {
                if ((sentSprint || sentSneak)) {
                    fail("sent sprint & sneak packet in the same tick");
                }
            }
        }
    }
}

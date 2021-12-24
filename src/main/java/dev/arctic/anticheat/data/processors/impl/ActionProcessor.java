package dev.arctic.anticheat.data.processors.impl;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;

@Getter
public class ActionProcessor extends Processor {

    private boolean sprinting, sneaking;

    public ActionProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if (event.getPacket().isEntityAction()) {
            final WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event.getPacket());
            switch (packet.getAction()) {
                case START_SPRINTING:
                    sprinting = true;
                    break;
                case STOP_SPRINTING:
                    sprinting = false;
                    break;
                case START_SNEAKING:
                    sneaking = true;
                    break;
                case STOP_SNEAKING:
                    sneaking = false;
                    break;
            }

        }
    }

    @Override
    public void handleSending(PacketEvent event) {

    }
}

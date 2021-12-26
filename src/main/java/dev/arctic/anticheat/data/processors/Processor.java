package dev.arctic.anticheat.data.processors;

import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.event.PacketEvent;


public abstract class Processor {

    public final PlayerData data;

    public Processor(PlayerData data) {
        this.data = data;
    }

    /**
     * @author Salers
     **/


    public abstract void handleReceive(PacketEvent event);

    public abstract void handleSending(PacketEvent event);
}

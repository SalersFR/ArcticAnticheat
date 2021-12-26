package dev.arctic.anticheat.data.processors.impl;

import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.Processor;
import dev.arctic.anticheat.packet.event.PacketEvent;
import lombok.Getter;

@Getter
public class ConnectionProcessor extends Processor {

    private long lastServerKeepAlive;
    private int keepAlivePing;

    public ConnectionProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleReceive(PacketEvent event) {
        if(event.getPacket().isKeepAlive()) {
            this.keepAlivePing = (int) (System.currentTimeMillis() - lastServerKeepAlive);
        }
    }

    @Override
    public void handleSending(PacketEvent event) {
        if(event.getPacket().isSendingKeepAlive()) {
            this.lastServerKeepAlive = System.currentTimeMillis();
        }

    }
}

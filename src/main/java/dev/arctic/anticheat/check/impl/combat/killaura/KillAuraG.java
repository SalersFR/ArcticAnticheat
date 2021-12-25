package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraG extends Check {

    public long lastAim;
    private double aimSpeed;
    private double attacks;
    private double swings;

    public KillAuraG(PlayerData data) {
        super(data, "KillAura", "G", "combat.killaura.g", "Checks for killaura accuration.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        //TODO CRAFTICAT (Old KillauraH)

    }
}

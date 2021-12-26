package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraH extends Check {


    private boolean sentAttack;
    private long timeDiff;
    private double diff;
    private double dist;


    public KillAuraH(PlayerData data) {
        super(data, "KillAura", "H", "combat.killaura.h", "Checks for failrate", true);
    }
    @Override
    public void handle(Packet packet, long time) {
        //TODO CRAFTICAT (old KillauraI)
    }
}

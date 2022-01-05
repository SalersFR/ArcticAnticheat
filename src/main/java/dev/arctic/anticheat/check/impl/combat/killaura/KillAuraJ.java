package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CombatProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.LocationUtils;
import net.jitse.npclib.api.NPC;

public class KillAuraJ extends Check {

    private NPC bot = Arctic.getInstance().getNpcLibInstance().createNPC();
    private boolean created = false;

    public KillAuraJ(PlayerData data) {
        super(data, "KillAura", "J", "combat.killaura.j", "Checks if the player hits the bot (impossible).", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isUseEntity()) {
            final CombatProcessor combatProcessor = data.getCombatProcessor();

            if (combatProcessor.getHitTicks() <= 1) {
                //attacked
                if (combatProcessor.getTarget().getUniqueId() == bot.getUniqueId()) {
                    //attacked the bot
                    if (++buffer > 10)
                        fail("attacked the bot.");
                } else if (buffer > 0) buffer -= 1.25;
            }

            debug("buffer=" + buffer);


        } else if (packet.isFlying()) {

            final CombatProcessor combatProcessor = data.getCombatProcessor();

            if (combatProcessor.getHitTicks() <= 50)
                bot.setLocation(LocationUtils.getBehindPlayer(data.getPlayer()));

            if (!created && combatProcessor.getHitTicks() <= 35) {
                bot.create();
                created = true;
            }


            if (created) {

                if (!bot.isShown(data.getPlayer()))
                    bot.show(data.getPlayer());

                if (combatProcessor.getHitTicks() > 35) {
                    bot.destroy();
                    created = false;

                }


            }


        }
    }
}

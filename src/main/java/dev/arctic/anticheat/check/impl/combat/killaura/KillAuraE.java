package dev.arctic.anticheat.check.impl.combat.killaura;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraE extends Check {


    private boolean interactAt, attack, interact, sentUseEntity, sentClose, sentAttack, sentInteract;

    /**
     * From an old anticheat
     */

    public KillAuraE(PlayerData data) {
        super(data, "KillAura", "E", "combat.killaura.e", "Checks for autoblock modules.", true);
    }


    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            this.interact = this.interactAt = this.attack = this.sentUseEntity = this.sentClose
                    = this.sentAttack = this.sentInteract = false;
        } else if(packet.isCloseWindow()) {
            sentClose = true;
        } else if(packet.isBlockPlace()) {
            if (this.sentUseEntity & !this.sentInteract) {
                fail("attack & interact");

            }
        } else if(packet.isUseEntity()) {
            if (!this.attack && (this.interact || this.interactAt)) {
                fail();
                this.interact = false;
                this.interactAt = false;
            }



            final WrapperPlayClientUseEntity wrapped = new WrapperPlayClientUseEntity(packet);

            switch (wrapped.getType()) {
                case ATTACK:
                    attack = true;
                    break;
                case INTERACT:
                    interact = true;
                    break;
                case INTERACT_AT:
                    interactAt = true;
                    break;
            }

            if (sentAttack && sentClose) {
                fail("close & attack");
            }

            this.attack = true;
            this.sentUseEntity = true;
        }

    }
}

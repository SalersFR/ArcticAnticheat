package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketReceiveEvent;
import arctic.ac.event.client.UseEntityEvent;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

public class KillAuraF extends Check {

    private boolean interactAt, attack, interact, sentUseEntity, sentClose, sentAttack, sentInteract;

    /**
     * From an old anticheat
     */

    public KillAuraF(PlayerData data) {
        super(data, "KillAura", "F", "combat.killaura.f", "Checks for autoblock modules.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {
            this.interact = false;
            this.interactAt = false;
            this.attack = false;
            this.sentUseEntity = false;
            this.sentClose = false;
            this.sentAttack = false;
            this.sentInteract = false;
        } else if (e instanceof PacketReceiveEvent) {

            final PacketReceiveEvent event = (PacketReceiveEvent) e;

            if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
                this.sentClose = true;
            } else if (event.getPacketType() == PacketType.Play.Client.BLOCK_PLACE) {


                if (this.sentUseEntity & !this.sentInteract) {
                    fail("attack & interact");

                }
            }
        } else if (e instanceof UseEntityEvent) {

            final UseEntityEvent event = (UseEntityEvent) e;
            if (!this.attack && (this.interact || this.interactAt)) {
                fail();
                this.interact = false;
                this.interactAt = false;
            }


            if (event.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                this.sentAttack = true;
            }

            if (event.getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT) {
                this.sentInteract = true;
            }

            if (event.getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT_AT) {
                this.interactAt = true;
            }

            if (sentAttack && sentClose) {
                fail("close & attack");
            }

            this.attack = true;
            this.sentUseEntity = true;


        }
    }
}

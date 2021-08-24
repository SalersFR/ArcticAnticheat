package polar.ac.check.checks.combat.killaura;

import com.comphenix.packetwrapper.WrapperPlayClientBlockPlace;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.FlyingEvent;
import polar.ac.event.client.PacketEvent;
import polar.ac.event.client.UseEntityEvent;

public class KillAuraF extends Check {

    private boolean interactAt,attack,interact,sentUseEntity,sentClose,sentAttack,sentInteract;

    /**
     * From an old anticheat
     */

    public KillAuraF(PlayerData data) {
        super(data, "KillAura", "F", "combat.killaura.f", true);
    }

    @Override
    public void handle(Event e) {
       if(e instanceof FlyingEvent) {
           this.interact = false;
           this.interactAt = false;
           this.attack = false;
           this.sentUseEntity = false;
           this.sentClose = false;
           this.sentAttack = false;
           this.sentInteract = false;
       } else if(e instanceof PacketEvent) {

           final PacketEvent event = (PacketEvent) e;

           if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
               this.sentClose = true;
           } else if(event.getPacketType() == PacketType.Play.Client.BLOCK_PLACE) {

               final WrapperPlayClientBlockPlace wrapper = new WrapperPlayClientBlockPlace(event.getContainer());

               if(this.sentUseEntity & !this.sentInteract)  {
                   fail("attack & interact" );

               }
           }
       } else if(e instanceof UseEntityEvent) {

           final UseEntityEvent event = (UseEntityEvent) e;
           if (!this.attack && (this.interact || this.interactAt)) {
               fail();
               this.interact = false;
               this.interactAt = false;
           }


           if(event.getAction() == EnumWrappers.EntityUseAction.ATTACK) {
               this.sentAttack = true;
           }

           if(event.getAction() == EnumWrappers.EntityUseAction.INTERACT) {
               this.sentInteract = true;
           }

           if(event.getAction() == EnumWrappers.EntityUseAction.INTERACT_AT) {
               this.interactAt = true;
           }

           if(sentAttack && sentClose) {
               fail("close & attack");
           }

           this.attack = true;
           this.sentUseEntity = true;


       }
    }
}

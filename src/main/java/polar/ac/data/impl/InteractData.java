package polar.ac.data.impl;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import polar.ac.Polar;
import polar.ac.data.PlayerData;
import polar.ac.data.PlayerDataManager;

@Getter
@Setter
public class InteractData {


    private NPC entityANPC;
    private int ticksSinceHurt;
    private boolean isDigging, isPlacing, isSprinting, isSneaking,isHurt;
    private long lastHitPacket;

    private Player player;

    private PlayerData data;

    private Entity target;

    public InteractData(PlayerData data) {
        this.player = data.getPlayer();
        this.data = data;
    }

    public void setDigging(boolean b) {
        this.isDigging = b;
    }

    public void setPlacing(boolean b) {
        this.isPlacing = b;
    }

    public void setEntityANPC(NPC npc) {
        this.entityANPC = npc;
    }
    public void setLastHitPacket(long l) {
        this.lastHitPacket = l;
    }

    public void handleActionPacket(WrapperPlayClientEntityAction wrapper) {
        switch (wrapper.getAction()) {
            case START_SPRINTING:
                this.isSprinting = true;
                break;
            case STOP_SPRINTING:
                this.isSprinting = false;
                break;
            case START_SNEAKING:
                this.isSneaking = true;
                break;
            case STOP_SNEAKING:
                this.isSneaking = false;
                break;
        }
    }

    public void handleUseEntity(WrapperPlayClientUseEntity wrapper) {
        if (wrapper.getType() == EnumWrappers.EntityUseAction.ATTACK) {
            this.target = wrapper.getTarget(data.getPlayer().getWorld());
        }

        if(target.getType() == EntityType.PLAYER) {
            PlayerData target = Polar.INSTANCE.getDataManager().getPlayerData((Player) wrapper.getTarget(data.getPlayer().getWorld()));
            target.getInteractData().setTicksSinceHurt(0);
        }
    }

    public void handleFlying() {
        this.ticksSinceHurt++;
        this.isHurt = ticksSinceHurt < 70;
    }
}

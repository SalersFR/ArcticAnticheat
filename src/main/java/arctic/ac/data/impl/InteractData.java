package arctic.ac.data.impl;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import arctic.ac.utils.WorldUtils;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

@Getter
@Setter
public class InteractData {


    private NPC entityANPC;
    private int ticksSinceHurt, ticksSinceSlime, ticksSinceTeleport, ticksSinceJoin, ticksSinceDigging, ticksSinceBow, ticksAlive;
    private boolean isDigging, isPlacing, isSprinting, isSneaking, isHurt, teleported, cinematic;
    private long lastHitPacket, lastTeleport;

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

        if (wrapper.getTarget(data.getPlayer().getWorld()) == null) return;

        if (target.getType() == EntityType.PLAYER) {
            PlayerData target = Arctic.INSTANCE.getDataManager().getPlayerData((Player) wrapper.getTarget(data.getPlayer().getWorld()));
            if (target == null) return;
            target.getInteractData().setTicksSinceHurt(0);
        }
    }

    public void handleDigging() {
        this.ticksSinceDigging = 0;
    }

    public void handleFlying() {
        this.ticksSinceHurt++;
        this.ticksSinceSlime++;
        this.ticksSinceTeleport++;
        this.isDigging = ticksSinceDigging < 15;
        this.ticksSinceDigging++;
        this.ticksSinceBow++;
        if (ticksSinceJoin < 1000)
            this.ticksSinceJoin++;
        this.isHurt = ticksSinceHurt <= 2;
        this.teleported = System.currentTimeMillis() - lastTeleport < 30;
        if (new WorldUtils().isOnACertainBlock(data.getPlayer(), "slime")) {
            this.ticksSinceSlime = 0;
        }

        this.ticksAlive++;

        if (data.getPlayer().isDead()) ticksAlive = 0;

    }

    public void handleOutTeleport(WrapperPlayServerEntityTeleport wrapper) {
        if (wrapper.getEntityID() == data.getPlayer().getEntityId()) {
            this.ticksSinceTeleport = 0;
            this.lastTeleport = System.currentTimeMillis();

        }
    }

    public void handleEventTeleport(final PlayerTeleportEvent event) {
        if (event.getPlayer().getEntityId() == data.getPlayer().getEntityId()) {
            this.ticksSinceTeleport = 0;
            this.lastTeleport = System.currentTimeMillis();

        }
    }

    public void onBow() {
        this.ticksSinceBow = 0;
    }

    public void onEDBE() {
        this.ticksSinceHurt = 0;
    }
}

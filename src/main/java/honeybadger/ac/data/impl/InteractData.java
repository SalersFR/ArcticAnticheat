package honeybadger.ac.data.impl;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import honeybadger.ac.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
@Setter
public class InteractData {


    private boolean isDigging, isPlacing, isSprinting, isSneaking;

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
        if(wrapper.getType() == EnumWrappers.EntityUseAction.ATTACK) {
            this.target = wrapper.getTarget(data.getPlayer().getWorld());
        }
    }
}

package honeybadger.ac.data.impl;

import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import honeybadger.ac.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class InteractData {


    private boolean isDigging,isPlacing,isSprinting,isSneaking;

    private Player player;

    public InteractData(PlayerData data) {
        this.player = data.getBukkitPlayerFromUUID();
    }

    public void setDigging(boolean b) {
        this.isDigging = b;
    }

    public void setPlacing(boolean b) {
        this.isPlacing = b;
    }

    public void handleActionPacket(WrapperPlayClientEntityAction wrapper) {
        switch(wrapper.getAction()) {
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
}

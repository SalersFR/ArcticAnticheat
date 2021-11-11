package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.ALocation;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class KillAuraH extends Check {

    public KillAuraH(PlayerData data) {
        super(data, "KillAura", "H", "combat.killaura.h", "Checks for killaura accuration",true);
    }
    private double aimSpeed;
    private double attacks;
    private double swings;
    public long lastAim;

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            UseEntityEvent attack = (UseEntityEvent) e;
            if (attack.getAction() == EnumWrappers.EntityUseAction.ATTACK) {

                //Attack
                boolean aiming = System.currentTimeMillis() - lastAim < 60;
                //if (aiming) data.getPlayer().sendMessage("Speed " + aimSpeed);
                boolean highSpeed = aimSpeed > 4 && aiming;
                //TODO check if target is moving.

                final EntityPlayer nms = ((CraftPlayer) data.getPlayer()).getHandle();
                int ping = nms.ping;

                Vector to;
                Vector from;

                boolean close = false;

                try {
                    to = (data.getPastEntityLocations().get((ping / 50)).toVector());
                    from = (data.getPastEntityLocations().get(5 + (ping / 50)).toVector());

                    double dist = to.distance(from);
                    if (dist < 0.4) close = true;

                } catch (IndexOutOfBoundsException exception) {

                }

                if (highSpeed && !close) attacks++;
            }
        }
        if (e instanceof ArmAnimationEvent) {
            //Swing
            swings++;
            if (swings >= 50) {
                double acc = attacks * 2;
                if (acc > 40) {
                    fail("acc " + acc);
                }
                swings = 0;
                attacks = 0;
            }
        }
        if (e instanceof RotationEvent) {
            RotationEvent rot = (RotationEvent) e;
            aimSpeed = ((aimSpeed * 4) + rot.getDeltaYaw()) / 5;
            lastAim = System.currentTimeMillis();
        }
    }
}

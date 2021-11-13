package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.event.client.UseEntityEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KillAuraJ extends Check {

    public KillAuraJ(PlayerData data) {
        super(data, "KillAura", "J", "combat.killaura.j", "Checks for extremely high aim with high attack accuracion",true);
    }
    public int swings;
    public int hits;
    public long lastHit;
    public long lastAim;
    public double aimSpeed;
    public double aimPitch;
    public double buffer;

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            swings++;
            if (swings >= 38) {
                int acc = hits;
                hits=0;
                swings=0;

                if (acc > 13) {
                    buffer++;
                    if (buffer > 1) {
                        fail("acc " + acc);
                    }
                } else if (buffer > 0) buffer-=0.1;
            }
        }
        if (e instanceof UseEntityEvent) {
            UseEntityEvent attack = (UseEntityEvent) e;
            if (!attack.getAction().equals(EnumWrappers.EntityUseAction.ATTACK)) return;

            long hit = System.currentTimeMillis();
            long lastHit = this.lastHit;
            double hitDelay = hit - lastHit;
            this.lastHit = hit;

            boolean aiming = System.currentTimeMillis() - lastAim < 60;
            boolean highSpeed = aimSpeed + aimPitch > 12 && aiming;

            if (hitDelay > 10 * 50) {
                swings--;
                return;
            }

            if (!highSpeed) return;

            hits++;
        }
        if (e instanceof RotationEvent) {
            RotationEvent rot = (RotationEvent) e;
            aimSpeed = ((aimSpeed * 4) + rot.getDeltaYaw()) / 5;
            aimPitch = ((aimPitch * 4) + rot.getDeltaPitch()) / 5;
            lastAim = System.currentTimeMillis();
        }
    }
}

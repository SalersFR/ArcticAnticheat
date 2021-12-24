package dev.arctic.anticheat.check.impl.combat.aim;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.RotationProcessor;
import dev.arctic.anticheat.packet.Packet;

public class AimH extends Check {

    private double lastPitchAtan, result;

    public AimH(PlayerData data) {
        super(data, "Aim", "H", "combat.aim.h", "Checks for invalid pitch values.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isRotation()) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean check = rotationProcessor.getDeltaYaw() <= 95 && rotationProcessor.getTicksSinceCinematic() >= 10;
            final double gcd = rotationProcessor.getExpandedGcdPitch();

            final double pitch = rotationProcessor.getPitch();

            debug("gcd=" + gcd + " buffer=" + buffer + " exempt=" + !check);

            if (Math.min(this.lastPitchAtan, Math.atan(pitch)) == this.result && gcd < 0x20000 && gcd > 0 && check && rotationProcessor.getDeltaPitch() < 20) {
                if (this.buffer < 15) buffer++;

                if (this.buffer > 3.25)
                    fail("gcd=" + gcd);
            } else this.buffer -= this.buffer > 0 ? 0.05 : 0;


            this.result = Math.min(this.lastPitchAtan, Math.atan(pitch));
            this.lastPitchAtan = Math.atan(pitch);


        }

    }
}

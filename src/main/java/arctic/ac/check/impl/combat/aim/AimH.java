package arctic.ac.check.impl.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.RotationProcessor;
import eu.salers.salty.packet.type.PacketType;

public class AimH extends Check {

    private double lastPitchAtan, result;

    public AimH(PlayerData data) {
        super(data, "Aim", "H", "combat.aim.h", "Checks for invalid pitch values.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_LOOK || packetType == PacketType.IN_POSITION_LOOK) {
            final RotationProcessor rotationProcessor = data.getRotationProcessor();

            final boolean check = rotationProcessor.getDeltaYaw() <= 95 && rotationProcessor.getTicksSinceCinematic() >= 10;
            final double gcd = rotationProcessor.getExpandedGcdPitch();

            final double pitch = rotationProcessor.getPitch();

            debug("gcd=" + gcd + " buffer=" + buffer + " exempt=" + !check);

            if (Math.min(this.lastPitchAtan, Math.atan(pitch)) == this.result && gcd < 0x20000 && gcd > 0 && check) {
                if (this.buffer < 15) buffer++;

                if (this.buffer > 3.25)
                    fail("gcd=" + gcd);
            } else this.buffer -= this.buffer > 0 ? 0.05 : 0;


            this.result = Math.min(this.lastPitchAtan, Math.atan(pitch));
            this.lastPitchAtan = Math.atan(pitch);


        }

    }
}

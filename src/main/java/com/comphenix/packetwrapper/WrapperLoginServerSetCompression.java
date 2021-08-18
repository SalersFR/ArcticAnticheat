/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperLoginServerSetCompression extends AbstractPacket {
    public static final PacketType TYPE =
            PacketType.Login.Server.SET_COMPRESSION;

    public WrapperLoginServerSetCompression() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperLoginServerSetCompression(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve HoneyBadger.
     * <p>
     * Notes: HoneyBadger is the max size of a packet before its compressed
     *
     * @return The current HoneyBadger
     */
    public int getHoneyBadger() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set HoneyBadger.
     *
     * @param value - new value.
     */
    public void setHoneyBadger(int value) {
        handle.getIntegers().write(0, value);
    }

}

package de.brabb3l.endustry;

import java.nio.*;

public class ModCall {
	  private static final ByteBuffer TEMP_BUFFER = ByteBuffer.allocate(4096);

	public static synchronized void onRemoveRadiation(int fid) {
	    if(io.anuke.mindustry.Vars.net.server()) {
	      io.anuke.mindustry.net.Packets.InvokePacket packet = io.anuke.arc.util.pooling.Pools.obtain(io.anuke.mindustry.net.Packets.InvokePacket.class, io.anuke.mindustry.net.Packets.InvokePacket::new);
	      packet.writeBuffer = TEMP_BUFFER;
	      packet.priority = (byte)0;
	      packet.type = (byte)100;
	      TEMP_BUFFER.position(0);
	      TEMP_BUFFER.putInt(fid);
	      packet.writeLength = TEMP_BUFFER.position();
	      io.anuke.mindustry.Vars.net.send(packet, io.anuke.mindustry.net.Net.SendMode.tcp);
	    }
	  }
	
}

package de.brabb3l.endustry;

import static io.anuke.mindustry.Vars.*;

import java.lang.reflect.*;
import java.nio.*;

import de.brabb3l.endustry.entities.effect.*;
import io.anuke.arc.*;
import io.anuke.arc.collection.*;
import io.anuke.arc.function.*;
import io.anuke.mindustry.core.GameState.*;
import io.anuke.mindustry.gen.*;
import io.anuke.mindustry.mod.*;
import io.anuke.mindustry.net.Packets.*;

public class Main extends Mod {

	public static String prefix = "endustry-";
	
	public static void log(Object object) {
		System.out.print("[Endustry] ");
		System.out.println(object);
	}

	@Override
	public void init() {
		Core.app.addListener(new ModApplication());
		
		try {
			ui.hudGroup.clear();
			ui.hudfrag = new ModHudFragment();
			ui.hudfrag.build(ui.hudGroup);
			ui.chatfrag.container().build(ui.hudGroup);
			ui.listfrag.build(ui.hudGroup);
			
			
			ModPlacementFragment modPlacementFragment = new ModPlacementFragment();
			modPlacementFragment.build(ui.hudGroup);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		log("Mod successfully initilized!");
	}

	@Override
	public void loadContent() {
		new ModFx().load();
		new ModStatusEffects().load();
		new ModItems().load();
		new ModBlocks().load();
		new ModLiquid().load();
		new ModTypeIDs().load();

		ModSounds.load();

		// unregister existing packet listeners
		try {
			Field field = io.anuke.mindustry.net.Net.class.getDeclaredField("clientListeners");

			field.setAccessible(true);
			ObjectMap<Class<?>, Consumer> clientListeners = (ObjectMap<Class<?>, Consumer>) field.get(net);

			log("Removed " + clientListeners.size + " client net listeners");
			clientListeners.remove(InvokePacket.class);

			net.handleClient(InvokePacket.class, packet -> {
				int id = packet.type;
				ByteBuffer buffer = packet.writeBuffer;

				packet.writeBuffer.position(0);

				try {
					RemoteReadClient.readPacket(buffer, id);
				} catch (Exception e) {
					if (id == 100) {
						try {
							int fid = buffer.getInt();
							Radiation.onRemoveRadiation(fid);
						} catch (Exception ex) {
							throw new RuntimeException("Failed to to read remote method 'onRemoveRadiation'!", e);
						}
					} else {
						throw new RuntimeException("Invalid read method ID: " + id + "");
					}
				}

			});
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		Radiation.radiantsGroup = entities.add(Radiation.class).enableMapping();
	}

	public static class ModApplication implements ApplicationListener {
		
		public void update() {
			if (state.is(State.playing)) {
				if (!state.isPaused()) {
					if (!state.isEditor()) {
						Radiation.radiantsGroup.update();
					}
				}
			}
		}
	}
	
}

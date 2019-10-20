package de.brabb3l.endustry;

import io.anuke.arc.*;
import io.anuke.arc.Application.*;
import io.anuke.arc.audio.*;
import io.anuke.mindustry.*;

public class ModSounds {

	public static Sound geiger;
	
	public static void load() {
		if (Vars.net.client())
			Core.assets.load(Core.app.getType() != ApplicationType.iOS ? "sounds/geiger.ogg" : "sounds/geiger.mp3", Sound.class).loaded = a -> geiger = (Sound)a;
	}
	
	public static void dispose() {
		 Core.assets.unload(Core.app.getType() != Application.ApplicationType.iOS ? "sounds/geiger.ogg" : "sounds/geiger.mp3");
		 geiger = null;
	}
	
}

package de.brabb3l.endustry;

import java.lang.reflect.*;

import io.anuke.arc.*;
import io.anuke.arc.collection.*;
import io.anuke.arc.collection.Array;
import io.anuke.arc.function.*;
import io.anuke.arc.scene.*;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.ui.fragments.*;

public class ModHudFragment extends HudFragment {
	
	public ModHudFragment() {
		Field eventsField = Reflections.findField(Events.class, "events");
		Reflections.makeAccessible(eventsField);
		ObjectMap<Object, Array<Consumer<?>>> events = (ObjectMap<Object, Array<Consumer<?>>>)Reflections.getField(eventsField, blockfrag);
		
		events.get(WorldLoadEvent.class).removeRange(8, 9);
		events.get(UnlockEvent.class).removeRange(1, 2);
	}

	@Override
	public void build(Group parent) {
		super.build(parent);
		
		parent.removeChild(parent.getChildren().get(parent.getChildren().size - 1));
	}
}

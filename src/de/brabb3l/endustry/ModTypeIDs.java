package de.brabb3l.endustry;

import de.brabb3l.endustry.entities.effect.*;
import io.anuke.mindustry.game.*;

public class ModTypeIDs implements ContentList {

	public static TypeID radiation;
	
	@Override
	public void load() {
		radiation = new TypeID("radiation", Radiation::new);
	}

}

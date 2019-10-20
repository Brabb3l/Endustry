package de.brabb3l.endustry;

import io.anuke.arc.graphics.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.type.*;

public class ModLiquid implements ContentList {

	public Liquid liquidUranium;
	
	@Override
	public void load() {
		liquidUranium = new Liquid(Main.prefix + "liquid-uranium", Color.valueOf("00ff22")){{
            temperature = 1f;
            viscosity = 1f;
            explosiveness = 0;
            heatCapacity = 0;
            effect = ModStatusEffects.radiated;
        }};
	}

}

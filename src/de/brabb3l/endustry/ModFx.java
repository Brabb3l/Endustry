package de.brabb3l.endustry;

import io.anuke.arc.graphics.*;
import io.anuke.arc.graphics.g2d.*;
import io.anuke.arc.math.*;
import io.anuke.mindustry.entities.Effects.*;
import io.anuke.mindustry.game.*;

public class ModFx implements ContentList {

	public static Color 
		lightRadiation = Color.valueOf("00ff77"),
		darkRadiation = Color.valueOf("16b560");
	
	public static Effect radiation, radiated;
	
	@Override
	public void load() {
		radiation = new Effect(50f, e -> {
            Draw.color(lightRadiation, darkRadiation, e.fin());

            Angles.randLenVectors(e.id, 2, 2f + e.fin() * 9f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
            });

            Draw.color();
        });
		
		radiated = new Effect(35f, e -> {
            Draw.color(lightRadiation, darkRadiation, e.fin());

            Angles.randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.4f);
            });

            Draw.color();
        });
	}

}

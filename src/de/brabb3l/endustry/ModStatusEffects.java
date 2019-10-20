package de.brabb3l.endustry;

import io.anuke.arc.math.*;
import io.anuke.mindustry.entities.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.type.*;

public class ModStatusEffects implements ContentList {

	public static StatusEffect radiated;

	@Override
	public void load() {
		radiated = new StatusEffect() {{
			damage = 0.03f;
			effect = ModFx.radiated;

			trans(() -> radiated, ((unit, time, newTime, result) -> {
				unit.damage(1f);
				Effects.effect(ModFx.radiated, unit.x + Mathf.range(unit.getSize() / 2f), unit.y + Mathf.range(unit.getSize() / 2f));
				result.set(this, Math.min(time + newTime, 900f));
			}));
		}};
	}

}

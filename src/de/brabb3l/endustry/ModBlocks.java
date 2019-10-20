package de.brabb3l.endustry;

import de.brabb3l.endustry.block.power.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.type.*;
import io.anuke.mindustry.world.blocks.*;
import io.anuke.mindustry.world.blocks.production.*;

public class ModBlocks implements ContentList {

	public static OreBlock oreUranium;
	public static RadioactiveNuclearReactor uraniumReactor;
	public static Drill decayDrill;
	
	@Override
	public void load() {
		oreUranium = new ModOreBlock(Main.prefix + "uranium", ModItems.uranium);
		
		uraniumReactor = new RadioactiveNuclearReactor(Main.prefix + "uranium-reactor"){{
            requirements(Category.power, ItemStack.with(
            		Items.lead, 300, 
            		Items.silicon, 200, 
            		Items.surgealloy, 250, 
            		ModItems.uranium, 150, 
            		Items.metaglass, 150));
            size = 3;
            health = 1500;
            itemDuration = 60 * 10;
            powerProduction = 50f;
            consumes.item(ModItems.uranium);
            heating = 0.03f;
            consumes.liquid(Liquids.cryofluid, 0.1f).update(false);
        }};
        
        decayDrill = new Drill(Main.prefix + "decay-drill"){{
            requirements(Category.production, ItemStack.with(
            		Items.lead, 150, 
            		Items.silicon, 100, 
            		Items.titanium, 150, 
            		ModItems.uranium, 500));
            drillTime = 160;
            size = 4;
            drawRim = true;
            hasPower = true;
            tier = 5;
            updateEffect = ModFx.radiation;
            updateEffectChance = 0.03f;
            drillEffect = ModFx.radiation;
            rotateSpeed = 0.1f;
            warmupSpeed = 0.01f;

            consumes.power(4f);
            consumes.liquid(Liquids.cryofluid, 0.1f).boost();
        }};
	}

}

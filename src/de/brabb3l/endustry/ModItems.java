package de.brabb3l.endustry;

import io.anuke.arc.graphics.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.type.*;

public class ModItems implements ContentList {
    public static Item uranium;

    @Override
    public void load(){
    	uranium = new Item(Main.prefix + "uranium", Color.valueOf("22cc22")){{
            type = ItemType.resource;
            hardness = 5;
            cost = 0.5f;
            alwaysUnlocked = true;
        }};
    }
}
package de.brabb3l.endustry;

import static io.anuke.mindustry.Vars.*;

import io.anuke.arc.*;
import io.anuke.arc.graphics.*;
import io.anuke.arc.graphics.g2d.*;
import io.anuke.mindustry.type.*;
import io.anuke.mindustry.world.blocks.*;

public class ModOreBlock extends OreBlock {

	public ModOreBlock(String name, Item itemDrop) {
		super(name);
		this.itemDrop = itemDrop;
	}

	@Override
	public void createIcons(PixmapPacker out, PixmapPacker editor) {
		for (int i = 0; i < variants; i++) {
			Pixmap image = new Pixmap(32, 32);
			PixmapRegion shadow = Core.atlas.getPixmap(itemDrop.name + (i + 1));

			int offset = image.getWidth() / tilesize - 1;
			Color color = new Color();

			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = offset; y < image.getHeight(); y++) {
					shadow.getPixel(x, y - offset, color);

					if (color.a > 0.001f) {
						color.set(0, 0, 0, 0.3f);
						image.draw(x, y, color);
					}
				}
			}

			image.draw(shadow);

			out.pack(name + (i + 1), image);
			editor.pack("editor-" + name + (i + 1), image); // EDIT

			if (i == 0) {
				editor.pack("editor-block-" + name + "-full", image);
				out.pack("block-" + name + "-full", image);
			}
		}
	}

}

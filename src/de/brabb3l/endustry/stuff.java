package de.brabb3l.endustry;

import java.awt.image.*;
import java.io.*;
import java.util.concurrent.atomic.*;

import javax.imageio.*;

import io.anuke.arc.*;
import io.anuke.arc.graphics.*;
import io.anuke.arc.graphics.g2d.*;

public class stuff {

	public static void dostuff() throws IOException {
		AtomicInteger ai = new AtomicInteger();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("atlasnames.txt"));
		
		Core.atlas.getRegionMap().forEach(entry -> {
			try {
				writer.write(entry.key + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			PixmapRegion region = Core.atlas.getPixmap(entry.key);
			
			final BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_ARGB);

			Color color = new Color();
			
			for (int x = 0; x < image.getWidth(); x++)
				for (int y = 0; y < image.getHeight(); y++) {
					Color.rgba8888ToColor(color, region.getPixel(x, y));
					image.setRGB(x, y, Color.argb8888(color));
				}
			
			try {
				ImageIO.write(image, "png", new FileOutputStream("images/" + entry.key + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		writer.close();
		
		Core.atlas.getTextures().forEach(texture -> {
			TextureData data = texture.getTextureData();
			if (!data.isPrepared())
				data.prepare();
			Pixmap map = data.consumePixmap();
			
			final BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);

			Color color = new Color();
			
			for (int x = 0; x < map.getWidth(); x++)
				for (int y = 0; y < map.getHeight(); y++) {
					Color.rgba8888ToColor(color, map.getPixel(x, y));
					image.setRGB(x, y, Color.argb8888(color));
				}
			
			try {
				ImageIO.write(image, "png", new FileOutputStream("images/" + ai.getAndIncrement() + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
}

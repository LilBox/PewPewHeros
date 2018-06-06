package RPG;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class MapGenerator {
	private int[][] quadrants;
	int finalBossCorner = (int) (Math.random() * 4);
	int size;

	public MapGenerator() {
		size = 128;
		quadrants = new int[128][128];
		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				quadrants[i][j] = (int) (Math.random() * 16);
			}
		}
	}

	public MapGenerator(int sideLength) {
		size = sideLength;
		quadrants = new int[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				quadrants[i][j] = (int) (Math.random() * 16);
			}
		}
	}

	public BufferedImage getTempImage(int x, int y, int width, int height) {
		int offsetx = x % 256;
		int offsety = y % 256;
		int basequadx = x / 256;
		int basequady = y / 256;
		int drawx = width / 256 + 2;
		int drawy = height / 256 + 2;
		BufferedImage tempImage = new BufferedImage(256 * (width / 256 + 2), 256 * (height / 256 + 2),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempImage.getGraphics();

		for (int i = -2; i < drawy; i++) {
			for (int j = -drawx; j < 2; j++) {
				if (basequadx - drawx / 2 + j < size && basequadx - drawx / 2 + j >= 0
						&& basequady + drawy / 2 - i < size && basequady + drawy / 2 - i >= 0) {

					g.drawImage(getImage(quadrants[basequadx - drawx / 2 + j][basequady + drawy / 2 - i]),
							(-256 * j + offsetx), 256 * i + offsety, null);
				}
			}
		}
		return tempImage;

	}

	public BufferedImage getImage(int mapChoice) {
		return ImageLoader.images[mapChoice];
	}

}
package game.gfx;

import game.Game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class Art {
	
	//Mobs
	public static Bitmap[][] zombieAni = cut("/art/mob/zombie.png", 32, 32);
	public static Bitmap entityFiller = load("/art/entity/entityFiller.png");
	public static Bitmap[][] entityFillerMoving = cut("/art/entity/entityFillerMoving.png", 32, 32);
	
	//walls
	public static Bitmap[][] wallTiles;
	public static int[][] wallTileColors;
	
	//floors
	public static Bitmap[][] floorTiles;
	public static int[][] floorTileColors;
	
	//shadows
	public static Bitmap shadow_north;
	public static Bitmap shadow_north_east;
	public static Bitmap shadow_east;
	public static Bitmap shadow_north_west;
	public static Bitmap shadow_west;
	
	//Particles
	public static Bitmap[][] particles;
	public static Bitmap[][] shineBig;
	
	//icons
	public static Bitmap[][] mapIcons;
	public static Bitmap panel;
	
	/**
	 * Return the bitmaps for a given piece of art, cut out from a sheet
	 * 
	 * @param string
	 *            Art piece name
	 * @param w
	 *            Width of a single bitmap
	 * @param h
	 *            Height of a single bitmap
	 * @return Bitmap array
	 */
	public static Bitmap[][] cut(String string, int w, int h) {
		return cut(string, w, h, 0, 0);
	}

	/**
	 * Return the bitmaps for a given piece of art, cut out from a sheet
	 * 
	 * @param string
	 *            Art piece name
	 * @param w
	 *            Width of a single bitmap
	 * @param h
	 *            Height of a single bitmap
	 * @param bx
	 * @param by
	 * @return Bitmap array
	 */
	private static Bitmap[][] cut(String string, int w, int h, int bx, int by) {
		try {
			BufferedImage bi = ImageIO.read(Game.class.getResource(string));

			int xTiles = (bi.getWidth() - bx) / w;
			int yTiles = (bi.getHeight() - by) / h;

			Bitmap[][] result = new Bitmap[xTiles][yTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					result[x][y] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h, result[x][y].pixels, 0, w);
				}
			}

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Bitmap[][] cutv(String string, int h) {
		try {
			BufferedImage bi = ImageIO.read(Game.class.getResource(string));

			int yTiles = bi.getHeight() / h;

			int xTiles = 0;
			Bitmap[][] result = new Bitmap[yTiles][];
			for (int y = 0; y < yTiles; y++) {
				List<Bitmap> row = new ArrayList<Bitmap>();
				int xCursor = 0;
				while (xCursor < bi.getWidth()) {
					int w = 0;
					while (xCursor + w < bi.getWidth() && bi.getRGB(xCursor + w, y * h) != 0xffed1c24) {
						w++;
					}
					if (w > 0) {
						Bitmap bitmap = new Bitmap(w, h);
						bi.getRGB(xCursor, y * h, w, h, bitmap.pixels, 0, w);
						row.add(bitmap);
					}
					xCursor += w + 1;
				}
				if (xTiles < row.size())
					xTiles = row.size();
				result[y] = row.toArray(new Bitmap[0]);
			}

			Bitmap[][] resultT = new Bitmap[xTiles][yTiles];
			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					try {
						resultT[x][y] = result[y][x];
					} catch (IndexOutOfBoundsException e) {
						resultT[x][y] = null;
					}
				}
			}

			return resultT;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int[][] getColors(Bitmap[][] tiles) {
		int[][] result = new int[tiles.length][tiles[0].length];
		for (int y = 0; y < tiles[0].length; y++) {
			for (int x = 0; x < tiles.length; x++) {
				result[x][y] = getColor(tiles[x][y]);
			}
		}
		return result;
	}

	private static int getColor(Bitmap bitmap) {
		int r = 0;
		int g = 0;
		int b = 0;
		for (int i = 0; i < bitmap.pixels.length; i++) {
			int col = bitmap.pixels[i];
			r += (col >> 16) & 0xff;
			g += (col >> 8) & 0xff;
			b += (col) & 0xff;
		}

		r /= bitmap.pixels.length;
		g /= bitmap.pixels.length;
		b /= bitmap.pixels.length;

		return 0xff000000 | r << 16 | g << 8 | b;
	}

	/**
	 * Load a bitmap resource by name
	 * 
	 * @param string
	 *            Resource name
	 * @return Bitmap on success, null on error
	 */
	private static Bitmap load(String string) {
		try {
			BufferedImage bi = ImageIO.read(Game.class.getResource(string));

			int w = bi.getWidth();
			int h = bi.getHeight();

			Bitmap result = new Bitmap(w, h);
			bi.getRGB(0, 0, w, h, result.pixels, 0, w);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Load a bitmap resource by name
	 * 
	 * @param string
	 *            Resource name
	 * @return BufferedImage on success, null on error
	 */
	private static BufferedImage loadBufferedImage(String string) {
		try {
			BufferedImage bi = ImageIO.read(Game.class.getResource(string));
			return bi;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}

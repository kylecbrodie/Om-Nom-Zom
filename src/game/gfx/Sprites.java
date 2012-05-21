package game.gfx;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import game.Game;

public class Sprites {
	
	/***
	 * Static list of all game artwork to be loaded (predefined)
	 */
	public static Sprite playerStatic = load("ZombieRun.png");
	public static Sprite floorTile = load("floorTile.png");
	public static Sprite wallTile = load("wallTile.png");
	
	/***
	 * cuts up an image into a two dimensional array of sprites
	 * 
	 * @param ref relative path to the required image file
	 * @param w width of the sprite
	 * @param h height of the sprite
	 * @return two dimensional array Sprite copy of the image
	 */
	private static Sprite[][] cut(String ref, int w, int h) {
        return cut(ref, 0, 0, w, h);
    }
	
	/***
	 * cuts up an image into a two dimensional array of images
	 * 
	 * @param ref relative path to the required image file
	 * @param xo starting x position on the image
	 * @param yo starting y position on the image
	 * @param w width of the sprite
	 * @param h height of the sprite
	 * @return two dimensional array of the cut up sprites
	 */
	private static Sprite[][] cut(String ref, int xo, int yo, int w, int h) {
        try {
            BufferedImage bi = ImageIO.read(Game.class.getResource("res/" + ref));

            int xTiles = (bi.getWidth() - xo) / w;
            int yTiles = (bi.getHeight() - yo) / h;

            Sprite[][] result = new Sprite[xTiles][yTiles];

            for (int x = 0; x < xTiles; x++) {
                for (int y = 0; y < yTiles; y++) {
                    result[x][y] = new Sprite(w, h);
                    bi.getRGB(xo + x * w, yo + y * h, w, h, result[x][y].pixels, 0, w);
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/***
	 * just loads an image and stores it in a Sprite
	 * 
	 * @param ref relative path to the required image file
	 * @return BufferedImage: the image
	 */
	private static Sprite load(String ref) {
        try {
            BufferedImage bi = ImageIO.read(Game.class.getResource("res/" + ref));

            int w = bi.getWidth();
            int h = bi.getHeight();

            Sprite result = new Sprite(w, h);
            bi.getRGB(0, 0, w, h, result.pixels, 0, w);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
	
	/***
	 * Cuts a image in the y direction only
	 * 
	 * @param ref relative path to the required image file
	 * @param h height of the sprite
	 * @return sprite array of the cut up image
	 */
	private static Sprite[] yCut(String ref, int h) {
        try {
            BufferedImage bi = ImageIO.read(Game.class.getResource("res/" + ref));

            int yTiles = bi.getHeight() / h;
            int w = bi.getWidth();

            Sprite[] result = new Sprite[yTiles];

            for (int y = 0; y < yTiles; y++) {
                result[y] = new Sprite(w, h);
                bi.getRGB(0, y * h, w, h, result[y].pixels, 0, w);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /***
     * returns the average color of every sprite as an integer
     * 
     * @param sprites the sprites to average
     * @returns average color of each sprite
     */
    private static int[][] getColors(Sprite[][] sprites) {
        int[][] result = new int[sprites.length];
        for (int i = 0; i < sprites.length; i++) {
        	result[x] = getColors(sprites[x]);
        }
        
        return result;
    }
    
    /***
     * returns the average color of every sprite as an integer
     * 
     * @param sprites the sprites to average
     * @returns average color of each sprite
     */
    private static int[] getColors(Sprite[] sprites) {
    	int[] result = new int[sprites.length];
    	for (int i = 0; i < sprites.length; i++) {
            	result[i] = getColor(sprites[i]);
        }
        
        return result;
    }
    
    /***
     * returns the average color of the sprite
     * 
     * @param sprite the sprite to average
     * @returns average color of the sprite
     */
    private static int getColor(Sprite sprite) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < sprite.pixels.length; i++) {
            int col = sprite.pixels[i];
            r += (col >> 16) & 0xff;
            g += (col >> 8) & 0xff;
            b += (col) & 0xff;
        }

        r /= sprite.pixels.length;
        g /= sprite.pixels.length;
        b /= sprite.pixels.length;

        return 0xff000000 | r << 16 | g << 8 | b;
    }
}
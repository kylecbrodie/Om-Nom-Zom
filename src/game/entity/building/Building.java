package game.entity.building;

import java.util.Random;

import game.gfx.Screen;
import game.level.Mapable;

/**
 * Represents the buildings in the city; where the humans will start off in and
 * mobs can move about inside.
 * 
 * @author Kyle Brodie
 * 
 */
public class Building implements Mapable {
	
	protected Random random = new Random();
	
	private static final int COLOR = 0xfff0f0f0; //some shade of gray...I think
	
	//public static final int MIN_BUILDING_DISTANCE = 1700; // Squared
	
	public int spawnTime = 0;
	
	private int x, y, width, height;
	
	/**
	 * Constructor
	 * 
	 * @param x
	 *            tile location of top left corner
	 * @param y
	 *            tile location of top left corner
	 * @param width
	 *            measured in tiles in tiles
	 * @param height
	 *            measured in tiles in tiles
	 */
	public Building(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//TODO: determine if there should be a spawn time or if it should be instantaneous.
		spawnTime = random.nextInt(0);
	}

	public void render(Screen s) {
	}

	public void tick() {
	}

	@Override
	public int getMapColor() {
		return COLOR;
	}

	@Override
	public int getMinimapColor() {
		return 0xff000000; //Black
	}

	@Override
	public int getTileWidth() {
		return width;
	}

	@Override
	public int getTileHeight() {
		return height;
	}

	@Override
	public String getName() {
		return "BUILDING";
	}
}
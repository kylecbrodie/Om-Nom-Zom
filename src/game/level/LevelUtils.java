package game.level;

import java.util.Random;

import game.entity.mob.Human;
import game.level.tile.WallTile;

/**
 * Helper class for making levels.
 * 
 * @author Kyle Brodie
 */
public class LevelUtils {
	
	/**
	 * Developer playground level. Just add tiles and stuff explicitly. no generation.
	 * @param level level to edit
	 */
	public static void createDevLevel(Level level) {
		/*for(int y = 0; y <= level.getHeight(); y++) {
			for(int x = 0; x <= level.getWidth(); x++) {
				level.setTile(x, y, new StreetTile());
			}
		}*/
		for(int x = 0; x <= level.getWidth(); x++) {
			level.setTile(x, 0, new WallTile());
			level.setTile(x, level.getHeight() - 1, new WallTile());
		}
		
		for(int y = 0; y <= level.getHeight() - 1; y++) {
			level.setTile(0, y, new WallTile());
			level.setTile(level.getWidth() - 1, y, new WallTile());
		}
		
		for(int y = 10; y <= level.getHeight() - 11; y++) {
			level.setTile(10, y, new WallTile());
		}
		
		for(int y = 10; y <= level.getHeight() - 11; y++) {
			level.setTile(30, y, new WallTile());
		}
		
		for(int y = 10; y <= level.getHeight() - 11; y++) {
			level.setTile(50, y, new WallTile());
		}
		
		Random r = new Random();
		for(int i = 0; i < 50; i++) {
			int x = r.nextInt(level.getWidth() - 1) + 1;
			int y = r.nextInt(level.getHeight() - 1) + 1;
			if(x == 10 || x == 30 || x == 50) {
				x += (r.nextInt(2) == 0) ? +1 : -1;
			}
			level.addEntity(new Human(x,y));
		}
		level.numHumans = 50;
	}
}
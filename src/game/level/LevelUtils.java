package game.level;

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
		for(int x = 0; x <= level.getWidth(); x++) {
			level.setTile(x, 0, new WallTile());
			level.setTile(x, level.getHeight(), new WallTile());
		}
		
		for(int y = 0; y <= level.getHeight(); y++) {
			level.setTile(0, y, new WallTile());
			level.setTile(level.getWidth(), y, new WallTile());
		}
		
		for(int y = 10; y <= level.getHeight() - 10; y++) {
			level.setTile(10, y, new WallTile());
		}
		
		level.addEntity(new Human(12,12));
		level.addEntity(new Human(9,10));
		level.addEntity(new Human(3,3));
		level.addEntity(new Human(7,7));
		level.addEntity(new Human(15,3));
	}
}
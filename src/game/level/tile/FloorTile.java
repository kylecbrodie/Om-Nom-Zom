package game.level.tile;

import game.gfx.Art;
import game.gfx.Screen;

/**
 * @author Kyle Brodie
 *
 */
public class FloorTile extends Tile {
	
	@Override
	public void render(Screen s) {
		s.draw(Art.floorTile, x * Tile.WIDTH, y * Tile.HEIGHT);
	}
}
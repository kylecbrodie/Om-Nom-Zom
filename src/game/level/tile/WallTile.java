package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Screen;

/**
 * @author Kyle Brodie
 */
public class WallTile extends Tile {
	
	@Override
	public boolean canPass(Entity e) {
		return false;
	}

	@Override
	public void render(Screen s) {
		s.draw(Art.wallTile, x * Tile.WIDTH, y * Tile.HEIGHT);
	}
}
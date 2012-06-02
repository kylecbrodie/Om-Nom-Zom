package game.level.tile;

import java.util.List;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;
import game.math.BBOwner;
import game.math.BoundingBox;

/**
 * 
 * @author Kyle Brodie
 *
 */
public abstract class Tile implements BBOwner {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	
	protected int x, y;
	protected Level level;
	
	public void init(Level l, int x, int y) {
		level = l;
		this.x = x;
		this.y = y;
		init();
	}
	
	public void init() {
	}
	
	public void render(Screen s) {
	}

	public void neighbourChanged(Tile tile) {
	}

	public void addClipBBs(List<BoundingBox> result, Entity e) {
		if (canPass(e))
			return;

		result.add(new BoundingBox(this, x * Tile.WIDTH, y * Tile.HEIGHT, (x + 1) * Tile.WIDTH, (y + 1) * Tile.HEIGHT));
	}
	
	public boolean canPass(Entity e) {
		return true;
	}

	@Override
	public void handleCollision(Entity e, double xa, double ya) {
	}
}
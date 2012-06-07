package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;
import game.math.Collidable;

/**
 * 
 * @author Kyle Brodie
 *
 */
public abstract class Tile implements Collidable {
	
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
	
	public abstract void render(Screen s);

	public void neighbourChanged(Tile tile) {
	}
	
	public boolean canPass(Entity e) {
		return true;
	}

	@Override
	public void handleCollision(Entity e, int dir, double vel) {
	}
}
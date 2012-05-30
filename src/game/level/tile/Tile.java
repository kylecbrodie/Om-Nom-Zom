package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.level.Level;
import game.level.Mapable;
import game.math.BB;
import game.math.BBOwner;

import java.util.List;
import java.util.Random;

public abstract class Tile implements BBOwner, Mapable {
	
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;

	public Level level;
	protected Random random = new Random();
	public int x, y;
	public int img = -1; // no image set yet
	public int minimapColor;
    
	public Tile() {
		if (img == -1) img = 0;
		minimapColor = Art.floorTileColor;
	}

	public void init(Level level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
	}

	public void render(Screen s) {
	    Bitmap floorTile = Art.floorTile;
	    s.draw(floorTile, x * Tile.WIDTH, y * Tile.HEIGHT);
	}
	
	public void renderTop(Screen s) {
	}
	
	public void neighbourChanged(Tile t) {
	}

	public void addClipBBs(List<BB> list, Entity e) {
		if (canPass(e))
			return;

		list.add(new BB(this, x * Tile.WIDTH, y * Tile.HEIGHT, (x + 1) * Tile.WIDTH, (y + 1) * Tile.HEIGHT));
	}

	public void handleCollision(Entity e, double xa, double ya) {
	}
	
	public boolean canPass(Entity e) {
		return true;
	}

	public boolean isBuildable() {
		return false;
	}

	public int getCost() {
		return 0;
	}
}
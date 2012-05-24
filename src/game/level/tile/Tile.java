package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.level.Editable;
import game.level.Level;
import game.math.BB;
import game.math.BBOwner;

import java.util.List;
import java.util.Random;

public abstract class Tile implements BBOwner, Editable {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;

	public Level level;
	protected Random random = new Random();
	public int x, y;
	public int img = -1; // no image set yet
	public int minimapColor;

	public boolean isShadowed_north;
	public boolean isShadowed_east;
	public boolean isShadowed_west;
	public boolean isShadowed_north_east;
	public boolean isShadowed_north_west;
    
	public Tile() {
		if (img == -1) img = random.nextInt(4);
		minimapColor = Art.floorTileColors[img & 7][img / 8];
	}

	public void init(Level level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
	}

	public boolean canPass(Entity e) {
		return true;
	}

	public void render(Screen screen) {
	    
	    Bitmap floorTile = (Art.floorTiles[img & 7][img / 8]).copy();
	    addShadows(floorTile);
	    screen.draw(floorTile, x * Tile.WIDTH, y * Tile.HEIGHT);
	    
	    
	}
	
	private void addShadows(Bitmap tile){
	    
	    if (isShadowed_north) {
	        tile.draw(Art.shadow_north, 0, 0);
	    } else {
	        if (isShadowed_north_east) {
	            tile.draw(Art.shadow_north_east, Tile.WIDTH - Art.shadow_east.getWidth(), 0);
	        } 
	        if (isShadowed_north_west) {
	            tile.draw(Art.shadow_north_west, 0, 0);
	        }
	    }
	    if (isShadowed_east) {
            tile.draw(Art.shadow_east, Tile.WIDTH - Art.shadow_east.getWidth() , 0);
        }
        if (isShadowed_west) {
            tile.draw(Art.shadow_west, 0, 0);
        }
	}

	public void addClipBBs(List<BB> list, Entity e) {
		if (canPass(e))
			return;

		list.add(new BB(this, x * Tile.WIDTH, y * Tile.HEIGHT, (x + 1)
				* Tile.WIDTH, (y + 1) * Tile.HEIGHT));
	}

	public void handleCollision(Entity entity, double xa, double ya) {
	}

	public boolean isBuildable() {
		return false;
	}

	public void neighbourChanged(Tile tile) {
	}

	public int getCost() {
		return 0;
	}

	public boolean castShadow() {
		return false;
	}

	public void renderTop(Screen screen) {
	}
	
	public void updateShadows(){
	}
}
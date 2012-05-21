package game.level.tile;

import game.gfx.Screen;
import game.gfx.Sprite;
import game.level.Level;

public class Tile {
	
	public boolean isPassable;
	public Sprite sprite;
	public Level level;
	public int x,y;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void init(Level l) {
		level = l;
	}
	
	public void render(Screen s) {
		s.drawSprite(sprite,x,y);
	}
}

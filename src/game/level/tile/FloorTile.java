package game.level.tile;

import game.level.Level;

public class FloorTile extends Tile {

	public static final int COLOR = 0xffffffff;
	public static final String NAME = "FLOOR";

	public FloorTile() {
		super();
	}
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		neighbourChanged(null);
	}

	@Override
	public boolean isBuildable() {
		return true;
	}
	
	@Override
	public int getMapColor() {
		return FloorTile.COLOR;
	}
	@Override
	public int getMinimapColor() {
		return minimapColor;
	}
	
	@Override
	public int getTileWidth() {
		return 1;
	}
	
	@Override
	public int getTileHeight() {
		return 1;
	}
	@Override
	public String getName() {
		return FloorTile.NAME;
	}
}
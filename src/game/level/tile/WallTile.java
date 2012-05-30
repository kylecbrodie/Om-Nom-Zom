package game.level.tile;

import java.util.List;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Screen;
import game.math.BB;

public class WallTile extends Tile {
	
	static final int WALLHEIGHT = 56;
	public static final int COLOR = 0xffff0000;
	private static final String NAME = "WALL";

	public WallTile() {
		img = 0;
		minimapColor = Art.wallTileColor;
	}

	public boolean canPass(Entity e) {
		return false;
	}

	public void addClipBBs(List<BB> list, Entity e) {
		if (canPass(e)) {
			return;
		}

		list.add(new BB(this, x * Tile.WIDTH, y * Tile.HEIGHT - 6, (x + 1) * Tile.WIDTH, (y + 1) * Tile.HEIGHT));
	}

	public void render(Screen s) {
		s.draw(Art.wallTile, x * Tile.WIDTH, y * Tile.HEIGHT - (WALLHEIGHT - Tile.HEIGHT));
	}

	public void renderTop(Screen s) {
		s.draw(Art.wallTile, x * Tile.WIDTH, y * Tile.HEIGHT - (WALLHEIGHT - Tile.HEIGHT), 32, 32);
	}

	public boolean isBuildable() {
		return false;
	}
	
	@Override
	public int getMapColor() {
		return WallTile.COLOR;
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
		return WallTile.NAME;
	}
}
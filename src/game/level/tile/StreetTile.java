package game.level.tile;
import game.gfx.Art;
import game.gfx.Screen;


public class StreetTile extends Tile{

	@Override
	public void render(Screen s) {
		s.draw(Art.streetTile, x*Tile.WIDTH, y*Tile.HEIGHT);
	}

}

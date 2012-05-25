package game.entity;

import game.entity.mob.Mob;
import game.gfx.Bitmap;

public class Player extends Mob {

	public Player(double x, double y, int team) {
		super(x, y, team);
	}

	@Override
	public Bitmap getSprite() {
		return null;
	}
	
}
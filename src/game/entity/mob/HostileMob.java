package game.entity.mob;

import game.gfx.Bitmap;

public class HostileMob extends Mob {

	public HostileMob(double x, double y, int team) {
		super(x, y, team);
	}

	@Override
	public Bitmap getSprite() {
		return null;
	}

}

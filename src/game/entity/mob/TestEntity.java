package game.entity.mob;

import game.gfx.Art;
import game.gfx.Bitmap;

public class TestEntity extends Mob {

	public TestEntity(double x, double y) {
		super(x, y,Team.Zombie);
		setPos(x, y);
		setStartHealth(1);
		dir = random.nextDouble() * Math.PI * 2;
		minimapColor = 0xffff0000;
		yOffs = 14;
	}

	public void tick() {
		super.tick();
		if (freezeTime > 0)
			return;

		dir += (random.nextDouble() - random.nextDouble()) * 0.2;
		xd += Math.cos(dir);
		yd += Math.sin(dir);
		if (!move(xd, yd)) {
			dir += (random.nextDouble() - random.nextDouble()) * 0.8;
		}
		xd *= 0.2;
		yd *= 0.2;
	}

	public void die() {
		super.die();
	}

	public Bitmap getSprite() {
		int facing = (int) ((Math.atan2(xd, -yd) * 4 / (Math.PI * 2) + 2.5)) & 3;

		return Art.entityFillerAni[facing][0];
	}
}

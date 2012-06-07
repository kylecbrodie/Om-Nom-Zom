package game.entity.mob;

import game.entity.Entity;

/**
 * @author Kyle Brodie
 *
 */
public abstract class Mob extends Entity {

	public Mob(int x, int y) {
		setPos(x,y);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
}
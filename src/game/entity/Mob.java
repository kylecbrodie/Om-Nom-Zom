package game.entity;

public abstract class Mob extends Entity {
	
	public void die() {
		remove();
	}
}

package game.entity;

import game.Keys;
import game.gfx.Sprites;
import game.util.DirectionalVector;

public class Player extends Mob {
	
	public int score;
	private Keys keys;
	
	public Player(Keys k) {
		keys = k;
		sprite = Sprites.playerStatic;
		motion.setSpeed(2.5f, 2.5f);
		motion.setOffset(sprite.w, sprite.h);
	}

	public void tick() {
		motion.setNextDirection(DirectionalVector.getDirection(keys.up.isDown, keys.down.isDown, keys.left.isDown, keys.right.isDown));
		motion.tick();
	}

	public void touchedBy(Entity entity) {}
	public void touchItem(ItemEntity itemEntity) {}
}
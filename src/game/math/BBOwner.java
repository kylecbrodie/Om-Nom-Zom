package game.math;

import game.entity.Entity;

public interface BBOwner {
	void handleCollision(Entity entity, double xa, double ya);
}

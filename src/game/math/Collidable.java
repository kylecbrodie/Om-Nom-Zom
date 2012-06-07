package game.math;

import game.entity.Entity;

/**
 * Enforces collision handling. All implementors are participating in collision
 * detection.
 * 
 * @author Kyle Brodie
 */
public interface Collidable {

	/**
	 * Called when this Object will collide with another Entity e.
	 * 
	 * @param e
	 *            the intersecting Entity
	 * @param dir
	 *            the direction of the Entity
	 * @param vel
	 *            the velocity of the Entity
	 */
	public void handleCollision(Entity e, int dir, double vel);
}
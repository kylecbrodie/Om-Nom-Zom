package game.math;

import game.entity.Entity;

/**
 * Enforces collision handling for those who
 * want to use bounding boxes
 * 
 * @author Kyle Brodie
 *
 */
public interface BBOwner {
	
	/**
	 * Called when the bounding box this owner owns intersects the bounding box of another Entity e
	 * 
	 * @param e the intersecting Entity
	 * @param xa the x acceleration of the Entity
	 * @param ya the y acceleration of the Entity
	 */
	public void handleCollision(Entity e, double xa, double ya);
}
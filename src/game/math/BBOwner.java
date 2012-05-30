package game.math;

import game.entity.Entity;

/***
 * Enforces collision detection between entities
 * 
 * @author Kyle Brodie
 * 
 */
public interface BBOwner {
	
	/**
	 * Logic to be executed in the event of a collision
	 * 
	 * @param other
	 *            the entity that is intersecting this object
	 * @param xa
	 *            the x acceleration of the other entity
	 * @param ya
	 *            the y acceleration of the other entity
	 */
	public void handleCollision(Entity other, double xa, double ya);
}
package game.math.predicates;

import game.entity.Entity;

/**
 * This predicate applies for an {@link Entity} if it is not removed.
 * 
 * @author Kyle Brodie
 */
public enum EntityCollides implements CollidablePredicate<Entity> {

	/**
	 * Singleton instance of {@link EntityCollides}.
	 */
	INSTANCE;

	@Override
	public boolean appliesTo(Entity item) {
		return !item.removed;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
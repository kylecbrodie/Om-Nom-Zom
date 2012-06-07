package game.math.predicates;

import game.math.Collidable;

/**
 * A CollidablePredicate is a generic predicate that tests whether an item that
 * is collidable (i.e. it implements {@link Collidable}) fulfills some
 * conditions.
 * 
 * @author Kyle Brodie
 */
public interface CollidablePredicate<T extends Collidable> {
	
	/**
	 * Returns true if the specific conditions of this CollidablePredicate apply
	 * to the given item (for example: the item is still valid, the item is
	 * still valid and extends a certain class, etc).
	 * 
	 * @param item
	 *            the item to be checked
	 * @return true if the predicate applies to the item, false otherwise
	 */
	boolean appliesTo(T item);
}
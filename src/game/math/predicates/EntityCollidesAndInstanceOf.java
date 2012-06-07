package game.math.predicates;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import game.entity.Entity;

/**
 * This predicate applies for an {@link Entity} if it intersects with the given
 * bounding box AND is an instance of at least one of the given classes.
 * <p>
 * See {@link EntityIntersectsBB} for the definition of intersection.
 * </p>
 * 
 * @author Catacomb-Snatch Project (http://www.catacombsnatch.net/)
 */
public class EntityCollidesAndInstanceOf implements CollidablePredicate<Entity> {
	
	private final Collection<Class<? extends Entity>> entityClasses;

	/**
	 * Constructs a predicate that is true if the given entity is an instance of
	 * class1 and intersects the bb.
	 * @param class1 first class to consider
	 */
	public EntityCollidesAndInstanceOf(Class<? extends Entity> class1) {
		this.entityClasses = new HashSet<Class<? extends Entity>>();
		this.entityClasses.add(class1);
	}
	
	/**
	 * Constructs a predicate that is true if the given entity is an instance of
	 * any of the provided classes and intersects the BoundingBox.
	 * 
	 * @param classes
	 *            The Entity has to be an instance of one class in this list of
	 *            classes
	 */
	public EntityCollidesAndInstanceOf(Class<? extends Entity>... classes) {
		this.entityClasses = new HashSet<Class<? extends Entity>>();
		entityClasses.addAll(Arrays.asList(classes));
	}

	@Override
	public boolean appliesTo(Entity item) {
		for (final Class<? extends Entity> entityClass : entityClasses) {
			if (entityClass.isInstance(item)) {
				return EntityCollides.INSTANCE.appliesTo(item);
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "EntityIntersectsOneOfBBPredicate: {" + "entityClasses = " + entityClasses + '}';
	}
}
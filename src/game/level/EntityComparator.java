package game.level;

import java.util.Comparator;

import game.entity.Entity;

/**
 * Sorts entities in row major order of their pixel locations NOT tile locations.
 * Put another way: the order of precedence is y location (which one is high up
 * on the screen) then x location (which one is farther left on the screen)
 * 
 * @author Kyle Brodie
 * 
 */
public class EntityComparator implements Comparator<Entity> {
	
	public int compare(Entity e0, Entity e1) {
		if (e0.pos.y < e1.pos.y)
			return -1;
		if (e0.pos.y > e1.pos.y)
			return +1;
		if (e0.pos.x < e1.pos.x)
			return -1;
		if (e0.pos.x > e1.pos.x)
			return +1;
		return 0;
	}
}
package game.level;

import game.entity.Entity;
import game.entity.particles.Particle;
import game.math.Vector2d;
import game.math.Vector2i;

import java.util.Comparator;

/**
 * Compares two entities to sort them in row major order.
 * 
 * @author Kyle Brodie
 *
 */
public class EntityComparator implements Comparator<Entity> {
	
	@Override
	public int compare(Entity e0, Entity e1) {
		if(e0 instanceof Particle || e1 instanceof Particle) {
			Vector2d e0Pos = e0.getDrawPos();
			Vector2d e1Pos = e1.getDrawPos();
			if (e0Pos.y < e1Pos.y)
				return -1;
			if (e0Pos.y > e1Pos.y)
				return +1;
			if (e0Pos.x < e1Pos.x)
				return -1;
			if (e0Pos.x > e1Pos.x)
				return +1;
			return 0;
		}
		Vector2i e0Pos = e0.getPos();
		Vector2i e1Pos = e1.getPos();
		if (e0Pos.y < e1Pos.y)
			return -1;
		if (e0Pos.y > e1Pos.y)
			return +1;
		if (e0Pos.x < e1Pos.x)
			return -1;
		if (e0Pos.x > e1Pos.x)
			return +1;
		return 0;
	}
}
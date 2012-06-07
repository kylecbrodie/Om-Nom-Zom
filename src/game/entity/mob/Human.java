package game.entity.mob;

import java.util.Set;
import java.util.TreeSet;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Screen;
import game.level.EntityComparator;
import game.math.Direction;
import game.math.Vector2d;
import game.math.Vector2i;

/**
 * @author Kyle Brodie
 */
public class Human extends Mob {

	private int ticksElapsed = 0;
	private int dirChangeTime;
	private boolean male;
	
	public Human(int x, int y) {
		super(x, y);
		male = random.nextBoolean();
		dirChangeTime = random.nextInt(3) * 60 + 60;
	}

	@Override
	public void tick() {
		Set<Entity> entities = level.getEntities(x - 2, y - 2, x + 2, y + 2);
		final Set<Zombie> zombies = new TreeSet<Zombie>(new EntityComparator());
		for(Entity e : entities) {
			if(e instanceof Zombie) {
				zombies.add((Zombie) e);
			}
		}
		Zombie closest = findClosest(zombies);
		if(closest != null) {
			dir = Direction.getOpposite(Direction.getDirectionTowards(getPos(), closest.getPos()));
		} else {
			if (ticksElapsed % dirChangeTime == 0) {
				dir = random.nextInt(4);
			}
		}
		if (ticksElapsed % 30 == 0) {
			if (!move(dir)) {
				dir = Direction.getOpposite(dir);
			}
		}

		ticksElapsed++;
		if (ticksElapsed > 60 * 30) {
			ticksElapsed = 0;
		}
	}
	
	private Zombie findClosest(Set<Zombie> zombies) {
		Vector2i pos = getPos();
		int shortestSquared = Integer.MAX_VALUE;
		Zombie shortest = null;
		for(Zombie z : zombies) {
			int dist = z.getPos().distSquared(pos);
			if(dist < shortestSquared) {
				shortestSquared = dist;
				shortest = z;
			}
		}
		return shortest;
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		if(male) {
			s.draw(Art.human_male[dir][0], pos.x, pos.y);
		} else {
			s.draw(Art.human_female[dir][0], pos.x, pos.y);
		}
	}
}
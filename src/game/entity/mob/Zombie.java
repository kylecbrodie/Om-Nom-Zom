package game.entity.mob;

import java.util.Set;
import java.util.TreeSet;

import game.entity.Entity;
import game.entity.particles.Particle;
import game.gfx.Art;
import game.gfx.Screen;
import game.level.EntityComparator;
import game.level.tile.Tile;
import game.math.Direction;
import game.math.Vector2d;
import game.math.Vector2i;

/**
 * @author Kyle Brodie
 */
public class Zombie extends Mob {

	private int ticksElapsed = 0;
	private int dirChangeTime;
	
	public Zombie(int x, int y) {
		super(x, y);
		dirChangeTime = random.nextInt(3) * 60 + 60;
	}

	@Override
	public void tick() {
		Set<Entity> entities = level.getEntities(x - 5, y - 5, x + 5, y + 5);
		final Set<Human> humans = new TreeSet<Human>(new EntityComparator());
		for(Entity e : entities) {
			if(e instanceof Human) {
				humans.add((Human) e);
			}
		}
		Human closest = findClosest(humans);
		if(closest != null) {
			dir = Direction.getDirectionTowards(getPos(), closest.getPos());
		} else {
			if (ticksElapsed % dirChangeTime == 0) {
				dir = random.nextInt(4);
			}
		}
		if (ticksElapsed % 45 == 0) {
			if (!move(dir)) {
				dir = Direction.getOpposite(dir);
			}
		}

		ticksElapsed++;
		if (ticksElapsed > 60 * 30) {
			ticksElapsed = 0;
		}
	}
	
	private Human findClosest(Set<Human> humans) {
		Vector2i pos = getPos();
		int shortestSquared = Integer.MAX_VALUE;
		Human shortest = null;
		for(Human h : humans) {
			int dist = h.getPos().distSquared(pos);
			if(dist < shortestSquared) {
				shortestSquared = dist;
				shortest = h;
			}
		}
		return shortest;
	}
	
	@Override
	public void collide(Entity entity, int dir) {
		if (entity instanceof Human){
			this.eat((Human) entity);
		}
	}
	
	public void eat(Human hum) {
		Vector2i pos = hum.getPos();
		hum.die();
		level.addEntity(new Zombie(pos.x, pos.y));
		int numParts = random.nextInt(8) + 3;
		for(int i = 0; i < numParts; i++) {
			level.addEntity(new Particle(pos.x, pos.y, pos.x * Tile.WIDTH + random.nextDouble() * (Tile.WIDTH / 2), pos.y * Tile.HEIGHT + random.nextDouble() * (Tile.HEIGHT / 2), random.nextDouble() - 0.5, random.nextDouble() * 4, Art.human_male[0][0]));
		}
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.zombie[dir][0], pos.x, pos.y);
	}
}
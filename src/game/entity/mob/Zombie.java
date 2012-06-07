package game.entity.mob;

import game.entity.Entity;
import game.entity.particles.Particle;
import game.gfx.Art;
import game.gfx.Screen;
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
		if(ticksElapsed % dirChangeTime == 0) {
			dir = random.nextInt(4);
		}
		
		if(ticksElapsed % 60 == 0) {
			if(!move(dir)) {
				dir = Direction.getOpposite(dir);
			}
		}
		
		ticksElapsed++;
		if (ticksElapsed > 60 * 30) {
			ticksElapsed = 0;
		}
	}
	
	@Override
	public void collide(Entity entity, int dir) {
		if (entity instanceof Human){
			this.eat((Human) entity);
		} else{
			System.out.println("Miss");
		}
	}
	
	public void eat(Human hum) {
		Vector2i pos = hum.getPos();
		hum.die();
		level.addEntity(new Zombie(pos.x, pos.y));
		int numParts = random.nextInt(20) + 1;
		for(int i = 0; i < numParts; i++) {
			level.addEntity(new Particle(pos.x * Tile.WIDTH + random.nextDouble() * (Tile.WIDTH / 2), pos.y * Tile.HEIGHT + random.nextDouble() * (Tile.HEIGHT / 2), random.nextDouble(), random.nextDouble() * 3, Art.human_male[0][0]));
		}
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.zombie[dir][0], pos.x, pos.y);
	}
}
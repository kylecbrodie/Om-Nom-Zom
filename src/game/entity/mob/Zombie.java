package game.entity.mob;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Screen;
import game.math.Direction;
import game.math.Vector2d;
import game.math.Vector2i;

/**
 * @author Kyle Brodie
 */
public class Zombie extends Mob {

	private int ticksElapsed = 0;
	private int frame = 0, dirChangeTime;
	
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
		
		if(ticksElapsed % 30 == 0) {
			frame++;
			if(frame >= Art.human_male[dir].length) {
				frame = 0;
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
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.zombie[frame][dir], pos.x, pos.y);
	}
}
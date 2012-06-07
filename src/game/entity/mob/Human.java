package game.entity.mob;

import game.gfx.Art;
import game.gfx.Screen;
import game.math.Direction;
import game.math.Vector2d;

/**
 * @author Kyle Brodie
 */
public class Human extends Mob {

	private int ticksElapsed = 0;
	private int frame = 0, dirChangeTime;
	private boolean male;
	
	public Human(int x, int y) {
		super(x, y);
		male = random.nextBoolean();
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
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		if(male) {
			s.draw(Art.human_male[frame][dir], pos.x, pos.y);
		} else {
			s.draw(Art.human_female[frame][dir], pos.x, pos.y);
		}
	}
}
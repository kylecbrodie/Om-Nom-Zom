package game.entity.mob;

import game.Keys;
import game.gfx.Art;
import game.gfx.Screen;
import game.math.Direction;
import game.math.Vector2d;

/**
 * 
 * @author Kyle Brodie
 *
 */
public class Player extends Mob {

	private Keys keys;
	private int frame = 0;
	private int ticksElapsed = 0;
	private static final double SPEED = 0.5;
	
	public Player(int x, int y, Keys keys) {
		super(x,y);
		this.keys = keys;
	}

	@Override
	public void tick() {
		super.tick();
		if(keys.up.isDown) {
			dir.dir = Direction.NORTH;
		}
		if(keys.right.isDown) {
			dir.dir = Direction.EAST;
		}
		if(keys.down.isDown) {
			dir.dir = Direction.SOUTH;
		}
		if(keys.left.isDown) {
			dir.dir = Direction.WEST;
		}
		
		if(!moving()) {
			move(dir.dir, SPEED);
		}
	
		if(ticksElapsed > 12) {
			ticksElapsed = 0;
			frame++;
			if(frame >= Art.entityFillerAni[dir.dir].length) {
				frame = 0;
			}
		}
		ticksElapsed++;
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.entityFillerAni[frame][dir.dir], pos.x, pos.y);
	}
}
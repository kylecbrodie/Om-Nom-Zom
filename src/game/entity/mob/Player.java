package game.entity.mob;

import game.Keys;
import game.gfx.Art;
import game.gfx.Screen;
import game.math.Direction;
import game.math.Vector2d;

/**
 * Player class :D
 * 
 * @author Kyle Brodie
 */
public class Player extends Zombie {

	private Keys keys;
	private int frame = 0;
	private int ticksElapsed = 0;
	
	public Player(int x, int y, Keys keys) {
		super(x,y);
		this.keys = keys;
	}

	@Override
	public void tick() {
		if(keys.lookUp.isDown) {
			dir = Direction.NORTH;
		} else if(keys.lookRight.isDown) {
			dir = Direction.EAST;
		} else if(keys.lookDown.isDown) {
			dir = Direction.SOUTH;
		} else if(keys.lookLeft.isDown) {
			dir = Direction.WEST;
		}
		if(keys.up.wasPressed()) {
			move(dir = Direction.NORTH);
		} else if(keys.right.wasPressed()) {
			move(dir = Direction.EAST);
		} else if(keys.down.wasPressed()) {
			move(dir = Direction.SOUTH);
		} else if(keys.left.wasPressed()) {
			move(dir = Direction.WEST);
		}else if(keys.up.isDown) {
			if(ticksElapsed % 20 == 0) {
				move(dir = Direction.NORTH);
			}
		} else if(keys.right.isDown) {
			if(ticksElapsed % 20 == 0) {
				move(dir = Direction.EAST);
			}
		} else if(keys.down.isDown) {
			if(ticksElapsed % 20 == 0) {
				move(dir = Direction.SOUTH);
			}
		} else if(keys.left.isDown) {
			if(ticksElapsed % 20 == 0) {
				move(dir = Direction.WEST);
			}
		}
		
		if(keys.attack.wasPressed()) {
			move(dir);
			move(dir);
		}
		
		ticksElapsed++;
		if (ticksElapsed > 60 * 30) {
			ticksElapsed = 0;
		}
	}
	
	public void nextFrame() {
		frame++;
		if(frame >= Art.player[dir].length) {
			frame = 0;
		}
	}
	
	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.player[dir][frame], pos.x, pos.y);
	}
}
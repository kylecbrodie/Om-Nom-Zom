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
public class Player extends Mob {

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
		}
	
		if(ticksElapsed > 12) {
			ticksElapsed = 0;
			frame++;
			if(frame >= Art.player.length) {
				frame = 0;
			}
		}
		ticksElapsed++;
	}

	@Override
	public void render(Screen s) {
		Vector2d pos = getDrawPos();
		s.draw(Art.player[frame][dir], pos.x, pos.y);
	}
}
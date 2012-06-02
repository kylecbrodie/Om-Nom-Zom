package game.entity;

import game.Keys;
import game.gfx.Art;
import game.gfx.Screen;

/**
 * @author kylebrodie
 *
 */
public class TestEntity extends Entity {

	private Keys keys;
	private double xa = 0.5, ya = 0.5;
	private int facing = 1;
	private int frame = 0;
	private int ticksElapsed = 0;
	
	public TestEntity(double x, double y, Keys keys) {
		this.x = x;
		this.y = y;
		this.keys = keys;
	}
	@Override
	public void tick() {
		if(keys.up.isDown) {
			move(0, -ya);
			facing = 0;
		}
		if(keys.down.isDown) {
			move(0, ya);
			facing = 2;
		}
		if(keys.left.isDown) {
			move(-xa, 0);
			facing = 3;
		}
		if(keys.right.isDown) {
			move(xa, 0);
			facing = 1;
		}
		if(ticksElapsed > 12) {
			ticksElapsed = 0;
			frame++;
			if(frame >= Art.entityFillerAni[facing].length) {
				frame = 0;
			}
		}
		ticksElapsed++;
	}

	@Override
	public void render(Screen s) {
		s.draw(Art.entityFillerAni[frame][facing], x, y);
	}
}
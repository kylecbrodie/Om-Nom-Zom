/**
 * 
 */
package game.entity.particles;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;

/**
 * Represents a particle to be generated on the fly
 * 
 * @author Kyle Brodie
 *
 */
public class Particle extends Entity {

	public double xa, ya, za;
	public double z;
	public int life;
	private Bitmap b;

	public Particle(double x, double y, double xa, double ya, Bitmap bm) {
		this.x = x;
		this.y = y;
		double pow = random.nextDouble() * 1 + 1;
		this.xa = xa * pow;
		this.ya = ya * pow;
		this.za = random.nextDouble() * 2 + 1.0;
		this.setSize(2, 2);
		physicsSlide = false;
		isBlocking = false;
		life = random.nextInt(20) + 50;
		
		b = new Bitmap(4,4);
		b.clear(Art.getColour(bm) | randomColorShift());
	}
	
	private int randomColorShift() {
		int r = random.nextInt(0x00FF0000) & 0x00FF0000;
		int g = random.nextInt(0x0000FF00) & 0x0000FF00;
		int b = random.nextInt(0x000000FF) & 0x000000FF;
		return 0xFF000000 | r | g | b;
		
	}

	@Override
	public void tick() {
		move(xa, ya);
		z += za;
		if (z < 0) {
			z = 0;
			xa *= 0.8;
			ya *= 0.8;
		} else {
			xa *= 0.98;
			ya *= 0.98;

		}
		za -= 0.2;
		if (--life < 0) {
			remove();
		}
	}

	@Override
	public void render(Screen s) {
		s.draw(b, x - 8, y - 8 - z);
	}
}
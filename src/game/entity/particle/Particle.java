package game.entity.particle;

import game.entity.Entity;
//import game.gfx.Art;
import game.gfx.Screen;

public class Particle extends Entity {
	
	public double xa, ya, za;
	public double z;
	public Entity owner;
	public int life;

	public Particle(double x, double y, double xa, double ya) {
		pos.set(x, y);
		double pow = random.nextDouble() * 1 + 1;
		this.xa = xa * pow;
		this.ya = ya * pow;
		this.za = random.nextDouble() * 2 + 1.0;
		this.setSize(2, 2);
		physicsSlide = false;
		isBlocking = false;
		life = random.nextInt(20) + 50;
	}

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
		if (--life < 0)
			remove();
	}

	public void render(Screen s) {
		//s.draw(Art.particles[1][0], pos.x - 8, pos.y - 8 - z);
	}
}
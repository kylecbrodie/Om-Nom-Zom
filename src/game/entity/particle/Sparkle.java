package game.entity.particle;

import game.gfx.Art;
import game.gfx.Screen;

public class Sparkle extends Particle {
	public int duration;

	public Sparkle(double x, double y, double xa, double ya) {
		super(x, y, xa, ya);
		duration = (life = random.nextInt(10) + 20) + 1;
	}

	public void render(Screen screen) {
		int anim = Art.shineBig.length - life * Art.shineBig.length / duration - 1;
		screen.draw(Art.shineBig[anim][0], pos.x - 8, pos.y - 8 - 4 - z);
	}
}
package game.gfx;

import game.Game;

import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite {

	private final BufferedImage[] frames;
	private int currentIndex;
	private int ticksSinceFrameChange;
	private int fps;

	public AnimatedSprite(BufferedImage[] imgs) {
		super(imgs[0]);
		frames = imgs;
		fps = frames.length;
	}
	
	public void tick() {
		int ticksPerFrame = Game.TICKS_PER_SEC / fps;
		if(ticksSinceFrameChange >= ticksPerFrame) {
			nextFrame();
			ticksSinceFrameChange = 0;
		} else
			ticksSinceFrameChange++;
	}
	
	public void nextFrame() {
		currentIndex++;
		currentIndex %= frames.length;
		image = frames[currentIndex];
	}

	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public int getFPS() {
		return fps;
	}
}
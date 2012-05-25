package game.entity;

import game.Game;
import game.gfx.Art;
import game.gfx.Screen;

public class TestEntity extends Entity {
	
	private int ticksPassed = 0;
	private int currentAni = 0;
	private int currentFrame = 0;
	
	public void tick() {
		ticksPassed++;
		if(ticksPassed >= 10) {
			nextFrame();
			currentFrame++;
		}
		if(ticksPassed >= 60) {
			nextAni();
			currentAni++;
		}
	}
	
	private void nextFrame() {
		if(currentFrame >= Art.zombieAni[0].length) {
			currentFrame = 0;
		} else {
			currentFrame++;
		}
	}

	private void nextAni() {
		if(currentAni >= Art.zombieAni.length) {
			currentAni = 0;
		} else {
			currentAni++;
		}
	}

	public void render(Screen s) {
		s.draw(Art.zombieAni[currentAni][currentFrame], Game.GAME_WIDTH/2, Game.GAME_HEIGHT / 2);
	}
}

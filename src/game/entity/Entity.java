package game.entity;

import java.util.Random;

import game.util.MotionVector;
import game.gfx.Screen;
import game.gfx.Sprite;
import game.level.Level;

public abstract class Entity {
	
	protected final Random random = new Random();
	public Sprite sprite;
	public MotionVector motion = new MotionVector();
	public boolean removed;
	public Level level;

	public void init(Level l) {
		level = l;
		motion.setBounds(l.width,l.height);
	}
	
	public void remove() {
		removed = true;
		level.remove(this);
	}
	
	public void render(Screen s) {
		s.drawSprite(sprite, motion.pos.x, motion.pos.y);
	}

	public abstract void tick();
	
	public abstract void touchedBy(Entity entity);
	public abstract void touchItem(ItemEntity itemEntity);
}

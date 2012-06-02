package game.entity;

import java.util.List;
import java.util.Random;

import game.gfx.Screen;
import game.level.Level;
import game.math.BBOwner;
import game.math.BoundingBox;
import game.math.Vector2d;

/**
 * Base class of all things that move and are visible
 * 
 * @author Kyle Brodie
 */
public abstract class Entity implements BBOwner {

	protected Random random = new Random();
	protected Level level;
	
	protected double x, y;
	public Vector2d radius = new Vector2d(10, 10);
	
	public boolean removed = false;
	protected boolean physicsSlide = false;
	public boolean isBlocking = true;
	
	/**
	 * X tile initial and Y tile initial
	 */
	public int xto, yto;
	
	public void init(Level l) {
		level = l;
		init();
	}
	
	public void init() {
	}
	
	public abstract void tick();
	
	protected boolean move(double xa, double ya) {
		List<BoundingBox> bbs = level.getClipBBs(this);
		if (physicsSlide || (xa==0||ya==0)) {
			boolean moved = false;
			if (!removed)
				moved |= partMove(bbs, xa, 0);
			if (!removed)
				moved |= partMove(bbs, 0, ya);
			return moved;
		} else {
			boolean moved = true;
			if (!removed)
				moved &= partMove(bbs, xa, 0);
			if (!removed)
				moved &= partMove(bbs, 0, ya);
			return moved;
		}
	}

	private boolean partMove(List<BoundingBox> bbs, double xa, double ya) {
		double oxa = xa;
		double oya = ya;
		BoundingBox from = getBB();

		BoundingBox closest = null;
		double epsilon = 0.01;
		for (int i = 0; i < bbs.size(); i++) {
			BoundingBox to = bbs.get(i);
			if (from.intersects(to))
				continue;

			if (ya == 0) {
				if (to.y0 >= from.y1 || to.y1 <= from.y0)
					continue;
				if (xa > 0) {
					double xrd = to.x0 - from.x1;
					if (xrd >= 0 && xa > xrd) {
						closest = to;
						xa = xrd - epsilon;
						if (xa < 0)
							xa = 0;
					}
				} else if (xa < 0) {
					double xld = to.x1 - from.x0;
					if (xld <= 0 && xa < xld) {
						closest = to;
						xa = xld + epsilon;
						if (xa > 0)
							xa = 0;
					}
				}
			}

			if (xa == 0) {
				if (to.x0 >= from.x1 || to.x1 <= from.x0)
					continue;
				if (ya > 0) {
					double yrd = to.y0 - from.y1;
					if (yrd >= 0 && ya > yrd) {
						closest = to;
						ya = yrd - epsilon;
						if (ya < 0)
							ya = 0;
					}
				} else if (ya < 0) {
					double yld = to.y1 - from.y0;
					if (yld <= 0 && ya < yld) {
						closest = to;
						ya = yld + epsilon;
						if (ya > 0)
							ya = 0;
					}
				}
			}
		}
		if (closest != null && closest.owner != null) {
			closest.owner.handleCollision(this, oxa, oya);
		}
		if (xa != 0 || ya != 0) {
			x += xa;
			y += ya;
			return true;
		}
		return false;
	}
	
	public abstract void render(Screen s);
	
	public BoundingBox getBB() {
		return new BoundingBox(this, x - radius.x, y - radius.y, x + radius.x, y + radius.y);
	}

	public boolean intersects(double x0, double y0, double x1, double y1) {
		return getBB().intersects(x0, y0, x1, y1);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya) {
		if (this.blocks(entity)) {
			this.collide(entity, xa, ya);
			entity.collide(this, -xa, -ya);
		}
	}
	
	public final boolean blocks(Entity e) {
		return isBlocking && e.isBlocking && shouldBlock(e) && e.shouldBlock(this);
	}

	protected boolean shouldBlock(Entity e) {
		return true;
	}

	public void collide(Entity entity, double xa, double ya) {
	}
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPos(Vector2d pos) {
		if(pos != null) {
			x = pos.x;
	    	y = pos.y;
		}
    }
	
	public void setSize(int xr, int yr) {
		radius = new Vector2d(xr, yr);
	}
	
	public Vector2d getPos() {
		return new Vector2d(x,y);
	}
	
	public void remove() {
		removed = true;
	}
}
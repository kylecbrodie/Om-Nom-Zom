package game.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;
import game.math.Collidable;
import game.math.Direction;
import game.math.DirectionalVector;
import game.math.Vector2d;
import game.math.Vector2i;

/**
 * Base class of all things that move and are visible
 * 
 * @author Kyle Brodie
 */
public abstract class Entity implements Collidable {

	protected Random random = new Random();
	protected Level level;
	
	/**
	 * offset from the tile position on the range of [-16,16]
	 * Allows for animation between tiles.
	 */
	protected double x = 0.0, y = 0.0;
	
	protected DirectionalVector dir = new DirectionalVector(Direction.NORTH, 0.0);
	
	/**
	 * Position on the level
	 */
	protected int xTile, yTile;
	
	public boolean removed = false;
	private boolean transitionMade = false;
	
	/**
	 * is participating in collision detection
	 */
	public boolean isBlocking = true;

	public void init(Level l) {
		level = l;
		init();
	}
	
	/**
	 * Initialization code goes here in subclasses
	 */
	public void init() {
	}
	
	/**
	 * All game logic runs here
	 */
	public void tick() {
		if (moving()) {
			y += dir.getY();
			x += dir.getX();
			if(transitionMade) {
				if(Double.compare(y, 0.0) == 0) {
					y = 0;
					x = 0;
					dir.vel = 0;
					transitionMade = false;
				} else if(Double.compare(x, 0.0) == 0) {
					x = 0;
					y = 0;
					dir.vel = 0;
					transitionMade = false;
				}
			}
			if (Math.abs(y) > 16 || Math.abs(x) > 16) {
				commitMove();
				transitionMade = true;
			}
		}
	}
	
	protected boolean moving() {
		return dir.vel != 0;
	}
	
	private final void commitMove() {
		List<Collidable> colids = level.getCollidables(this, xTile + (int)dir.getX(), yTile + (int)dir.getY());
		for(Iterator<Collidable> i = colids.iterator(); i.hasNext();) {
			Collidable next = i.next();
			if(next instanceof Entity) {
				Entity e = (Entity)next;
				if(e.blocks(this)) { //cannot move forward
					handleCollision(e, e.dir.dir, e.dir.vel);
					dir.vel = 0.0;
					return;
				}
			}
		}
		level.moveEntity(this, xTile + dir.getXOffset(), yTile + dir.getYOffset());
		System.out.println("entity moved");
		y = -y;
		y += (y > 0) ? -1 : 1;
		x = -x--;
		x += (x > 0) ? -1 : 1;
	}
	
	/**
	 * 
	 * @param dir directions from the Facing class
	 * @param vel 
	 * @return if the move was successfully started.
	 */
	protected boolean move(int dir, double vel) {
		List<Collidable> colids = level.getCollidables(this, xTile + (int)this.dir.getX(), yTile + (int)this.dir.getY());
		boolean moved = false;
		if(colids == null) {
			return moved;
		}
		for(Iterator<Collidable> i = colids.iterator(); i.hasNext();) {
			Collidable next = i.next();
			if(next instanceof Tile) {
				Tile t = (Tile) next;
				if(!t.canPass(this)) {
					return moved;
				}
			}
			if(next instanceof Entity) {
				Entity e = (Entity) next;
				if(e.dir.vel != 0) {
					if(e.dir.dir == Direction.getOpposite(this.dir.dir)) {
						handleCollision(e, e.dir.dir, e.dir.vel);
					}
				}
			}
		}
		
		this.dir.vel = vel;
		this.dir.dir = dir;
		return true;
	}
	
	public abstract void render(Screen s);

	@Override
	public void handleCollision(Entity e, int dir, double vel) {
		if (this.blocks(e)) {
			this.collide(e, dir, vel);
			e.collide(this, this.dir.dir, this.dir.dir);
		}
	}
	
	public final boolean blocks(Entity e) {
		return isBlocking && e.isBlocking && shouldBlock(e) && e.shouldBlock(this);
	}

	protected boolean shouldBlock(Entity e) {
		return true;
	}

	/**
	 * Called when a collision occurs
	 * 
	 * @param entity the entity you are colliding with
	 * @param dir their direction
	 * @param vel their velocity
	 */
	public void collide(Entity entity, int dir, double vel) {
	}
	
	/**
	 * Sets the Tile that this entity resides on
	 * 
	 * @param x pos of tile
	 * @param y pos of tile
	 */
	public void setPos(int x, int y) {
		xTile = x;
		yTile = y;
	}
	
	/**
	 * Sets the Tile that this entity resides on
	 * 
	 * @param pos vector of the tile position
	 */
	public void setPos(Vector2i pos) {
		if(pos != null) {
			x = pos.x;
	    	y = pos.y;
		}
    }
	
	/**
	 * Returns the Tile position of this entity
	 * @return {@link Vector2i} of the position
	 */
	public Vector2i getPos() {
		return new Vector2i(xTile, yTile);
	}
	
	/**
	 * Gets the double position of the entity. This will differ from getPos()
	 * when the entity is in the middle of an animation.
	 * 
	 * @return current drawing position
	 */
	public Vector2d getDrawPos() {
		return new Vector2d(xTile * Tile.WIDTH + x, yTile * Tile.HEIGHT * y);
	}
	
	/**
	 * Removes the entity from the game.
	 */
	public void remove() {
		removed = true;
	}
}
package game.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;
import game.math.Collidable;
import game.math.Direction;
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
	
	protected int dir = Direction.NORTH;
	
	/**
	 * Position on the level in tiles
	 */
	protected int x, y;
	
	public boolean removed = false;
	
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
	public abstract void tick();
	
	/**
	 * Moves the entity to the tile next to yours in the provided the direction.
	 * 
	 * @param dir directions from the Direction class
	 * @return if the move was successful.
	 */
	protected boolean move(int dir) {
		List<Collidable> colids = level.getCollidables(this, x + Direction.getXOffset(dir), y + Direction.getYOffset(dir));
		if(colids == null) {
			return false;
		}
		for(Iterator<Collidable> i = colids.iterator(); i.hasNext();) {
			Collidable next = i.next();
			if(next instanceof Tile) {
				Tile t = (Tile) next;
				if(!t.canPass(this)) {
					return false;
				}
			}
			if(next instanceof Entity) {
				Entity e = (Entity) next;
				handleCollision(e, e.dir);
				if(e.blocks(this)) {
					return false;
				}
			}
		}
		
		level.moveEntity(this, x + Direction.getXOffset(dir), y + Direction.getYOffset(dir));
		return true;
	}
	
	public abstract void render(Screen s);

	@Override
	public void handleCollision(Entity e, int dir) {
		if (this.blocks(e)) {
			this.collide(e, dir);
			e.collide(this, this.dir);
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
	 */
	public void collide(Entity entity, int dir) {
	}
	
	/**
	 * Sets the Tile that this entity resides on
	 * 
	 * @param x pos of tile
	 * @param y pos of tile
	 */
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
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
		return new Vector2i(x, y);
	}
	
	/**
	 * Gets the double position of the entity. This will differ from getPos()
	 * when the entity is in the middle of an animation.
	 * 
	 * @return current drawing position
	 */
	public Vector2d getDrawPos() {
		return new Vector2d(x * Tile.WIDTH, y * Tile.HEIGHT);
	}
	
	/**
	 * Removes the entity from the game.
	 */
	public void remove() {
		removed = true;
	}
}
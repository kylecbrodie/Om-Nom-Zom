package game.level;

import game.entity.Entity;
import game.gfx.Art;
import game.gfx.Screen;
import game.level.tile.FloorTile;
import game.level.tile.Tile;
import game.math.Collidable;
import game.math.Rect;
import game.math.Vector2d;
import game.math.Vector2i;
import game.math.predicates.CollidablePredicate;
import game.math.predicates.EntityCollides;
import game.math.predicates.EntityCollidesAndInstanceOf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Maintains the map and all entity locations.
 * Handles collision
 * 
 * @author Kyle Brodie
 *
 */
public class Level {

	private LinkedList<Tile>[] map;
	private List<Entity>[] entityMap;
	private List<Entity> entities;
	
	final int[] neighbourOffsets;
	private final int width,height;
	
	@SuppressWarnings("unchecked")
	public Level(int width, int height) {
		//TODO: add minimap code
		this.width = width;
		this.height = height;
		neighbourOffsets = new int[] { -1, 1, -width, -width + 1, -width - 1, width, width + 1, width - 1 };
		
		entities = new ArrayList<Entity>();
		initializeTileMap();
		
		entityMap = new ArrayList[width * height];
		for (int i = 0; i < width * height; i++) {
			entityMap[i] = new ArrayList<Entity>();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initializeTileMap() {
		map = new LinkedList[width * height];

		// All of the lists need to be setup before adding the tiles
		for (int i = 0; i < map.length; i++) {
				map[i] = new LinkedList<Tile>();
		}
		
		// Each list will contain a floor tile
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setTile(x, y, new FloorTile());
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int size() {
		return width * height;
	}
	
	/**
	 * Changes the current active tile at xy to the given tile and updates the
	 * surrounding tiles
	 * 
	 * @param x the x location of the 
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Tile tile) {
		int index = x + y * width;
		if(index >= 0 && index < map.length) {
			map[index].addLast(tile);
			tile.init(this, x, y);

			updateTiles(x, y, tile);
		}
	}
	
	public Tile getTile(int x, int y) {
		int index = x + y * width;
		if(index >= 0 && index < map.length) {
			return map[index].peekLast();
		} else {
			return null;
		}
	}

	public Tile getTile(Vector2d pos) {
		int x = (int) pos.x / Tile.WIDTH;
		int y = (int) pos.y / Tile.HEIGHT;
		return getTile(x, y);
	}

	public void removeTile(int x, int y) {
		int index = x + y * width;
		if (index >= 0 && index < map.length && map[index].size() > 1) {
			map[index].removeLast();
			updateTiles(x, y, map[index].peekLast());
		}
	}

	private void updateTiles(int x, int y, Tile tile) {
		for (int offset : neighbourOffsets) {
			final int nbIndex = x + (y * width) + offset;
			if (nbIndex >= 0 && nbIndex < width * height) {
				final Tile neighbour = map[nbIndex].peekLast();
				if (neighbour != null) {
					neighbour.neighbourChanged(tile);
				}
			}
		}
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.removed) {
				e.tick();
			}
			if (e.removed) {
				entities.remove(i--);
				removeFromEntityMap(e);
			}
		}
	}
	
	public boolean moveEntity(Entity e, int x, int y) {
		if(x < 0 || x >= width) {
			return false;
		}
		if(y < 0 || y >= height) {
			return false;
		}
		removeFromEntityMap(e);
		insertToEntityMap(e, x, y);
		return true;
	}
	
	private void updateMinimap() {
		// TODO implement
	}
	
	public void render(Screen s, int xScroll, int yScroll) {
		int x0 = xScroll / Tile.WIDTH;
		int y0 = yScroll / Tile.HEIGHT;
		int x1 = (xScroll + s.getWidth()) / Tile.WIDTH;
		int y1 = (yScroll + s.getHeight()) / Tile.HEIGHT;
		if (xScroll < 0) {
			x0--;
		}
		if (yScroll < 0) {
			y0--;
		}

		//Set<Entity> visibleEntities = getEntities(xScroll - Tile.WIDTH, yScroll - Tile.HEIGHT, xScroll + s.getWidth() + Tile.WIDTH, yScroll + s.getHeight() + Tile.HEIGHT);

		s.setOffset(-xScroll, -yScroll);

		renderTiles(s, x0, y0, x1, y1);

		for (Entity e : entities/*visibleEntities*/) {
			e.render(s);
		}

		//renderTopOfWalls(s, x0, y0, x1, y1);
		
		s.setOffset(0, 0);
		
		updateMinimap();
		renderHUDAndMinimap(s, x0, y0);
	}

	private void renderTiles(Screen s, int x0, int y0, int x1, int y1) {
		// go through each currently visible cell
		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {

				// draw floor outside the level
				if (x < 0 || x >= width || y < 0 || y >= height) {
					s.draw(Art.floorTile, x * Tile.WIDTH, y * Tile.HEIGHT);
					continue;
				}
				
				int tilesIndex = x + y * width;
				
				if (tilesIndex >= 0 && tilesIndex < map.length) {
					
					for (int i = 0; i < map[tilesIndex].size(); i++) {
						map[tilesIndex].get(i).render(s);
					}
				}
			}
		}
	}
	
	private void renderHUDAndMinimap(Screen s, int x0, int y0) {
		// TODO implement
	}

	public void addEntity(Entity e) {
		e.init(this);
		entities.add(e);
		Vector2i pos = e.getPos();
		insertToEntityMap(e, pos.x, pos.y);
	}
	
	public void removeEntity(Entity e) {
		e.removed = true;
	}
	
	public void insertToEntityMap(Entity e, int x, int y) {
		e.setPos(x, y);
		int index = x + y * width;
		if(index >= 0 && index < entityMap.length) {
			entityMap[index].add(e);
		}
	}

	public void removeFromEntityMap(Entity e) {
		Vector2i pos = e.getPos();
		int index = pos.x + pos.y * width;
		if(index >= 0 && index < entityMap.length) {
			entityMap[index].remove(e);
		}
	}
	
	/**
	 * Returns all entities and tiles at the tile (x,y) that will collide with
	 * the provided entity
	 * 
	 * @param e
	 *            the entity that is requesting the collidables to see if
	 *            certain other entities block it in particular
	 * @param x
	 *            tile loc
	 * @param y
	 *            tile loc
	 * @return All collidable objects in the next tile or null if the tile is
	 *         not in the level.
	 */
	public List<Collidable> getCollidables(Entity e, int x, int y) {
		
		List<Collidable> result = new ArrayList<Collidable>();
		Tile t = getTile(x, y);
		
		//Level edge
		if(t == null) {
			return null;
		}
		
		if(!t.canPass(e)) {
			result.add(t);
		}
		
		Set<Entity> visibleEntities = new TreeSet<Entity>(new EntityComparator());//getEntities(new Rect(x,y,0,0));
		for (Entity ent : entityMap[x + y * width]) {
			result.add(ent);
		}
		
		for (Entity ee : visibleEntities) {
			if (ee != e && ee.blocks(e)) {
				result.add(ee);
			}
		}
		return result;
	}
	
	public Set<Entity> getEntities(Rect r) {
		return getEntities(r.x0, r.y0, r.x1, r.y1);
	}
	
	public Set<Entity> getEntities(Rect r, Class<? extends Entity> c) {
		return getEntities(r.x0, r.y0, r.x1, r.y1, c);
	}

	public Set<Entity> getEntities(int x0, int y0, int x1, int y1) {
		return getEntities(x0, y0, x1, y1, EntityCollides.INSTANCE);
	}

	public Set<Entity> getEntities(int x0, int y0, int x1, int y1, Class<? extends Entity> c) {
		return getEntities(x0, y0, x1, y1, new EntityCollidesAndInstanceOf(c));
	}

	public Set<Entity> getEntities(double xx0, double yy0, double xx1, double yy1, CollidablePredicate<Entity> predicate) {
		final int x0 = Math.max((int) (xx0) / Tile.WIDTH, 0);
		final int x1 = Math.min((int) (xx1) / Tile.WIDTH, width - 1);
		final int y0 = Math.max((int) (yy0) / Tile.HEIGHT, 0);
		final int y1 = Math.min((int) (yy1) / Tile.HEIGHT, height - 1);

		final Set<Entity> result = new TreeSet<Entity>(new EntityComparator());

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				for (Entity e : entityMap[x + y * width]) {
					if (predicate.appliesTo(e)) {
						result.add(e);
					}
				}
			}
		}

		return result;
	}
}
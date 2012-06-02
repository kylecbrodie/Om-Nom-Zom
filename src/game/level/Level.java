package game.level;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.tile.FloorTile;
import game.level.tile.Tile;
import game.math.BoundingBox;
import game.math.Vector2d;
import game.math.predicates.BBPredicate;
import game.math.predicates.EntityIntersectsBB;
import game.math.predicates.EntityIntersectsBBAndInstanceOf;

import java.util.ArrayList;
import java.util.Iterator;
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

				Vector2d pos = e.getPos();
				int xtn = (int) (pos.x - e.radius.x) / Tile.WIDTH;
				int ytn = (int) (pos.y - e.radius.y) / Tile.HEIGHT;
				if (xtn != e.xto || ytn != e.yto) {
					removeFromEntityMap(e);
					insertToEntityMap(e);
				}
			}
			if (e.removed) {
				entities.remove(i--);
				removeFromEntityMap(e);
			}
		}
	}
	
	public void render(Screen s) {
		for(LinkedList<Tile> list : map) {
			for(Iterator<Tile> iter = list.iterator(); iter.hasNext();) {
				iter.next().render(s);
			}
		}
		for(Entity e : entities) {
			e.render(s);
		}
	}
	
	public void addEntity(Entity e) {
		e.init(this);
		entities.add(e);
		insertToEntityMap(e);
	}
	
	public void removeEntity(Entity e) {
		e.removed = true;
	}
	
	public void insertToEntityMap(Entity e) {
		Vector2d pos = e.getPos();
		e.xto = (int) (pos.x - e.radius.x) / Tile.WIDTH;
		e.yto = (int) (pos.y - e.radius.y) / Tile.HEIGHT;

		int x1 = e.xto + (int) (e.radius.x * 2 + 1) / Tile.WIDTH;
		int y1 = e.yto + (int) (e.radius.y * 2 + 1) / Tile.HEIGHT;

		for (int y = e.yto; y <= y1; y++) {
			if (y < 0 || y >= height) {
				continue;
			}
			for (int x = e.xto; x <= x1; x++) {
				if (x < 0 || x >= width) {
					continue;
				}
				entityMap[x + y * width].add(e);
			}
		}
	}

	public void removeFromEntityMap(Entity e) {
		int x1 = e.xto + (int) (e.radius.x * 2 + 1) / Tile.WIDTH;
		int y1 = e.yto + (int) (e.radius.y * 2 + 1) / Tile.HEIGHT;

		for (int y = e.yto; y <= y1; y++) {
			if (y < 0 || y >= height) {
				continue;
			}
			for (int x = e.xto; x <= x1; x++) {
				if (x < 0 || x >= width) {
					continue;
				}
				entityMap[x + y * width].remove(e);
			}
		}
	}
	
	public List<BoundingBox> getClipBBs(Entity e) {
		List<BoundingBox> result = new ArrayList<BoundingBox>();
		BoundingBox bb = e.getBB().grow(Tile.WIDTH);

		int x0 = (int) (bb.x0 / Tile.WIDTH);
		int y0 = (int) (bb.y0 / Tile.HEIGHT);
		int x1 = (int) (bb.x1 / Tile.WIDTH);
		int y1 = (int) (bb.y1 / Tile.HEIGHT);

		//Level edge barriers
		result.add(new BoundingBox(null, 0, 0, 0, height * Tile.HEIGHT));
		result.add(new BoundingBox(null, 0, 0, width * Tile.WIDTH, 0));
		result.add(new BoundingBox(null, width * Tile.WIDTH, 0, width * Tile.WIDTH, height * Tile.HEIGHT));
		result.add(new BoundingBox(null, 0, height * Tile.HEIGHT, width * Tile.WIDTH, height * Tile.HEIGHT));

		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= height) {
				continue;
			}
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= width) {
					continue;
				}
				getTile(x, y).addClipBBs(result, e);
			}
		}

		Set<Entity> visibleEntities = getEntities(bb);
		for (Entity ee : visibleEntities) {
			if (ee != e && ee.blocks(e)) {
				result.add(ee.getBB());
			}
		}

		return result;
	}
	
	public Set<Entity> getEntities(BoundingBox bb) {
		return getEntities(bb.x0, bb.y0, bb.x1, bb.y1);
	}
	
	public Set<Entity> getEntities(BoundingBox bb, Class<? extends Entity> c) {
		return getEntities(bb.x0, bb.y0, bb.x1, bb.y1, c);
	}

	public Set<Entity> getEntities(double x0, double y0, double x1, double y1) {
		return getEntities(x0, y0, x1, y1, EntityIntersectsBB.INSTANCE);
	}

	public Set<Entity> getEntities(double x0, double y0, double x1, double y1, Class<? extends Entity> c) {
		return getEntities(x0, y0, x1, y1, new EntityIntersectsBBAndInstanceOf(c));
	}

	public Set<Entity> getEntities(double xx0, double yy0, double xx1, double yy1, BBPredicate<Entity> predicate) {
		final int x0 = Math.max((int) (xx0) / Tile.WIDTH, 0);
		final int x1 = Math.min((int) (xx1) / Tile.WIDTH, width - 1);
		final int y0 = Math.max((int) (yy0) / Tile.HEIGHT, 0);
		final int y1 = Math.min((int) (yy1) / Tile.HEIGHT, height - 1);

		final Set<Entity> result = new TreeSet<Entity>(new EntityComparator());

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				for (Entity e : entityMap[x + y * width]) {
					if (predicate.appliesTo(e, xx0, yy0, xx1, yy1)) {
						result.add(e);
					}
				}
			}
		}

		return result;
	}
}
package game.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import game.Options;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.entity.mob.Player;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.level.tile.FloorTile;
import game.level.tile.Tile;
import game.math.BB;
import game.math.Vec2;
import game.math.predicates.BBPredicate;
import game.math.predicates.EntityIntersectsBB;
import game.math.predicates.EntityIntersectsBBAndInstanceOf;

public class Level {

	private Random random = new Random();

	private LinkedList<Tile>[] tiles;
	private List<Vec2> spawnPoints;
	private Bitmap minimap;
	private boolean largeMap = false, smallMap = false;

	final int[] neighbourOffsets;

	public int TARGET_SCORE = 100;
	public final int width, height;
	public List<Entity>[] entityMap;
	public List<Entity> entities = new ArrayList<Entity>();
	public int maxMonsters;

	public int[][] monsterDensity;
	public int densityTileWidth = 5;
	public int densityTileHeight = 5;

	public int playerScore = 0;

	@SuppressWarnings("unchecked")
	public Level(int width, int height) {
		neighbourOffsets = new int[] { -1, 1, -width, -width + 1, -width - 1,
				width, width + 1, width - 1 };
		this.width = width;
		this.height = height;

		int denseTileArrayWidth;
		int denseTileArrayHeight;
		if (width % 3 == 0) {
			denseTileArrayWidth = width / densityTileWidth;
		} else {
			denseTileArrayWidth = width / densityTileWidth + 1;
		}

		if (height % 3 == 0) {
			denseTileArrayHeight = height / densityTileHeight;
		} else {
			denseTileArrayHeight = height / densityTileHeight + 1;
		}

		monsterDensity = new int[denseTileArrayWidth][denseTileArrayHeight];

		minimap = new Bitmap(width, height);

		largeMap = height > 64 || width > 64;
		smallMap = height < 64 && width < 64;

		initializeTileMap();

		spawnPoints = new ArrayList<Vec2>();

		entityMap = new List[width * height];
		for (int i = 0; i < width * height; i++) {
			entityMap[i] = new ArrayList<Entity>();
		}
	}

	@SuppressWarnings("unchecked")
	private void initializeTileMap() {
		tiles = new LinkedList[width * height];

		// All of the lists need to be setup before adding the tiles
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + y * width] = new LinkedList<Tile>();
			}
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
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Tile tile) {
		tiles[x + y * width].addLast(tile);
		tile.init(this, x, y);

		updateTiles(x, y, tile);
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return null;
		}
		return tiles[x + y * width].peekLast();
	}

	public Tile getTile(Vec2 pos) {
		int x = (int) pos.x / Tile.WIDTH;
		int y = (int) pos.y / Tile.HEIGHT;
		return getTile(x, y);
	}

	public void removeTile(int x, int y) {
		int index = x + y * width;
		if (tiles[index].size() > 1) {
			tiles[index].removeLast();
			updateTiles(x, y, tiles[index].peekLast());
		}

	}

	private void updateTiles(int x, int y, Tile tile) {
		for (int offset : neighbourOffsets) {
			final int nbIndex = x + (y * width) + offset;
			if (nbIndex >= 0 && nbIndex < width * height) {
				final Tile neighbour = tiles[nbIndex].peekLast();
				if (neighbour != null) {
					neighbour.neighbourChanged(tile);
				}
			}
		}
	}

	/**
	 * Add a possible spawn point for the humans
	 * 
	 * @param x
	 * @param y
	 * @param team
	 */
	public void addSpawnPoint(int x, int y) {
			spawnPoints.add(new Vec2(x, y));

	}

	public Vec2 getRandomSpawnPoint() {
		int index = random.nextInt(spawnPoints.size() - 1);
			return spawnPoints.get(index);
	}

	/**
	 * Checks that there are spawn points for new humans
	 * 
	 * @return true if all players can spawn
	 */
	public boolean canSpawn() {
		return !spawnPoints.isEmpty();
	}

	public void insertToEntityMap(Entity e) {
		e.xto = (int) (e.pos.x - e.radius.x) / Tile.WIDTH;
		e.yto = (int) (e.pos.y - e.radius.y) / Tile.HEIGHT;

		int x1 = e.xto + (int) (e.radius.x * 2 + 1) / Tile.WIDTH;
		int y1 = e.yto + (int) (e.radius.y * 2 + 1) / Tile.HEIGHT;

		for (int y = e.yto; y <= y1; y++) {
			if (y < 0 || y >= height)
				continue;
			for (int x = e.xto; x <= x1; x++) {
				if (x < 0 || x >= width)
					continue;
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

	public Set<Entity> getEntities(BB bb) {
		return getEntities(bb.x0, bb.y0, bb.x1, bb.y1);
	}

	public Set<Entity> getEntities(double x0, double y0, double x1, double y1) {
		return getEntities(x0, y0, x1, y1, EntityIntersectsBB.INSTANCE);
	}

	public Set<Entity> getEntities(BB bb, Class<? extends Entity> c) {
		return getEntities(bb.x0, bb.y0, bb.x1, bb.y1, c);
	}

	public Set<Entity> getEntities(double x0, double y0, double x1, double y1,
			Class<? extends Entity> c) {
		return getEntities(x0, y0, x1, y1, new EntityIntersectsBBAndInstanceOf(
				c));
	}

	public Set<Entity> getEntities(double xx0, double yy0, double xx1,
			double yy1, BBPredicate<Entity> predicate) {
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

	public Set<Entity> getEntitiesSlower(double xx0, double yy0, double xx1,
			double yy1, Class<? extends Entity> c) {
		final Set<Entity> result = new TreeSet<Entity>(new EntityComparator());
		final BBPredicate<Entity> predicate = new EntityIntersectsBBAndInstanceOf(
				c);

		for (Entity e : this.entities) {
			if (predicate.appliesTo(e, xx0, yy0, xx1, yy1)) {
				result.add(e);
			}
		}

		return result;
	}

	public void addEntity(Entity e) {
		e.init(this);
		entities.add(e);
		insertToEntityMap(e);
	}

	public void addMob(Mob m, int xTile, int yTile) {
		updateDensityList();
		if (monsterDensity[(int) (xTile / densityTileWidth)][(int) (yTile / densityTileHeight)] < Options.difficulty
				.getAllowedMobDensity()) {
			addEntity(m);
		}
	}

	public void removeEntity(Entity e) {
		e.removed = true;
	}

	public void updateDensityList() {
		for (int x = 0; x < monsterDensity.length; x++) {
			for (int y = 0; y < monsterDensity[x].length; y++) {
				int entityNumb = getEntities(x * densityTileWidth * Tile.WIDTH,
						y * densityTileHeight * Tile.HEIGHT,
						x * (densityTileWidth + 1) * Tile.WIDTH,
						y * (densityTileHeight + 1) * Tile.HEIGHT).size();
				monsterDensity[x][y] = entityNumb;
			}
		}
	}

	public void tick() {
		// for (int i = 0; i < tickItems.size(); i++) {
		// tickItems.get(i).tick(this);
		// }

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.removed) {
				e.tick();

				int xtn = (int) (e.pos.x - e.radius.x) / Tile.WIDTH;
				int ytn = (int) (e.pos.y - e.radius.y) / Tile.HEIGHT;
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
		// TODO: add check for win state
	}

	public void render(Screen screen, int xScroll, int yScroll) {
		int x0 = xScroll / Tile.WIDTH;
		int y0 = yScroll / Tile.HEIGHT;
		int x1 = (xScroll + screen.getWidth()) / Tile.WIDTH;
		int y1 = (yScroll + screen.getHeight()) / Tile.HEIGHT;
		if (xScroll < 0) {
			x0--;
		}
		if (yScroll < 0) {
			y0--;
		}

		Set<Entity> visibleEntities = getEntities(xScroll - Tile.WIDTH, yScroll - Tile.HEIGHT, xScroll + screen.getWidth() + Tile.WIDTH, yScroll + screen.getHeight() + Tile.HEIGHT);

		screen.setOffset(-xScroll, -yScroll);

		renderTiles(screen, x0, y0, x1, y1);

		for (Entity e : visibleEntities) {
			e.render(screen);
			// this renders players carrying something
			e.renderTop(screen);
		}

		renderTopOfWalls(screen, x0, y0, x1, y1);

		screen.setOffset(0, 0);

		updateMinimap();
		renderPanelAndMinimap(screen, x0, y0);
	}

	private void renderTiles(Screen screen, int x0, int y0, int x1, int y1) {
		// go through each currently visible cell
		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {

				// draw sand outside the level
				if (x < 0 || x >= width || y < 0 || y >= height) {
					screen.draw(Art.floorTile, x * Tile.WIDTH, y * Tile.HEIGHT);
					continue;
				}

				for (int i = 0; i < tiles[x + y * width].size(); i++) {
					tiles[x + y * width].get(i).render(screen);
				}
			}
		}
	}

	private void renderTopOfWalls(Screen screen, int x0, int y0, int x1, int y1) {
		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= width || y < 0 || y >= height) {
					continue;
				}
				for (int i = 0; i < tiles[x + y * width].size(); i++) {
					tiles[x + y * width].get(i).renderTop(screen);
				}
			}
		}
	}

	private void updateMinimap() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int i = x + y * width;
				minimap.setPixel(i, getTile(x, y).minimapColor);
			}
		}

		addIconsToMinimap();
	}

	private void addIconsToMinimap() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.removed) {
				if (e.minimapIcon >= 0) {
					int x = (int) (e.pos.x / Tile.WIDTH);
					int y = (int) (e.pos.y / Tile.WIDTH);
					if (x >= 0 && y >= 0 && x < width && y < height) {
						minimap.draw(Art.mapIcons[e.minimapIcon % 4][e.minimapIcon / 4], x - 2, y - 2);
					}
				}
			}
		}
	}

	private void renderPanelAndMinimap(Screen screen, int x0, int y0) {
		Bitmap displaymap = new Bitmap(64, 64);

		if (largeMap) {
			displaymap = calculateLargeMapDisplay(x0, y0);
		} else if (smallMap) {
			displaymap = calculateSmallMapDisplay();
		} else {
			displaymap = minimap;
		}

		//screen.draw(Art.panel, 0, screen.getHeight() - 80);
		screen.draw(displaymap, 429, screen.getHeight() - 80 + 5);
	}

	private Bitmap calculateLargeMapDisplay(int x0, int y0) {

		Bitmap largeMap = new Bitmap(64, 64);
		int locx = x0 + 8;
		int locy = y0 + 8;

		int drawx = 0, drawy = 0;
		int donex = 0, doney = 0;
		int diffx = 0, diffy = 0;

		if (width < 64) {
			diffx = (64 - width) / 2;
		}
		if (height < 64) {
			diffy = (64 - height) / 2;
		}

		if (locx < 32 || width < 64) {
			drawx = 0;
		} else if (locx > (width - 32)) {
			drawx = width - 64;
		} else {
			drawx = locx - 32;
		}

		if (locy < 32 || height < 64) {
			drawy = 0;
		} else if (locy > (height - 32)) {
			drawy = height - 64;
		} else {
			drawy = locy - 32;
		}

		for (int y = 0; y < 64; y++) {
			if (y < diffy || y >= (64 - diffy)) {
				for (int x = 0; x < 64; x++) {
					largeMap.setPixel(x + (y * 64), Art.floorTileColor);
				}
			} else {
				for (int x = 0; x < 64; x++) {
					if (x < diffx || x > (64 - diffx)) {
						largeMap.setPixel(x + (y * 64), Art.floorTileColor);
					} else {
						if (((drawx + donex) + (drawy + doney) * width) < minimap.totalPixels() - 1) {
							largeMap.setPixel(x + (y * 64), minimap.getPixel((drawx + donex) + (drawy + doney) * width));
						}
						donex++;
					}
				}
				donex = 0;
				doney++;
			}
		}

		return largeMap;
	}

	private Bitmap calculateSmallMapDisplay() {

		Bitmap smallMap = new Bitmap(64, 64);
		int smallx = 0, smally = 0;
		for (int y = 0; y < 64; y++) {
			for (int x = 0; x < 64; x++) {
				if (x >= (32 - width / 2) && x <= (32 + width / 2)
						&& y >= (32 - height / 2) && y < (32 + height / 2) - 1) {
					smallMap.setPixel(x + y * 64,
							minimap.getPixel(smallx + smally * width));
					smallx++;
				} else
					smallMap.setPixel(x + y * 64, Art.floorTileColor);
			}
			smallx = 0;
			if (y >= (32 - height / 2) && y < (32 + height / 2) - 1) {
				smally++;
			}
		}
		return smallMap;
	}

	public List<BB> getClipBBs(Entity e) {
		List<BB> result = new ArrayList<BB>();
		BB bb = e.getBB().grow(Tile.WIDTH);

		int x0 = (int) (bb.x0 / Tile.WIDTH);
		int x1 = (int) (bb.x1 / Tile.WIDTH);
		int y0 = (int) (bb.y0 / Tile.HEIGHT);
		int y1 = (int) (bb.y1 / Tile.HEIGHT);

		result.add(new BB(null, 0, 0, 0, height * Tile.HEIGHT));
		result.add(new BB(null, 0, 0, width * Tile.WIDTH, 0));
		result.add(new BB(null, width * Tile.WIDTH, 0, width * Tile.WIDTH,
				height * Tile.HEIGHT));
		result.add(new BB(null, 0, height * Tile.HEIGHT, width * Tile.WIDTH,
				height * Tile.HEIGHT));

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

	public void placeTile(int x, int y, Tile tile, Player player) {
		if (!getTile(x, y).isBuildable()) {
			return;
		}
		if (player != null) {
			setTile(x, y, tile);
		}
	}

	// counts how many of a certain entity class are in play
	public <T> int countEntities(Class<T> entityType) {
		int count = 0;
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			if (entityType.isInstance(it.next())) {
				count++;
			}
		}
		return count;
	}
}
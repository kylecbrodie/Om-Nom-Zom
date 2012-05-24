package game.entity.building;

import game.Options;
import game.entity.Entity;
import game.entity.mob.Mob;
import game.entity.mob.Team;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.level.Editable;
import game.level.tile.Tile;

/**
 * Spawner entity. A sarcophage which spawns enemies of a given type onto the
 * field.
 */
public abstract class EntitySpawner<T extends Entity> extends Building implements Editable {
	/** Spawn interval in frames */
	public static final int SPAWN_INTERVAL = 60 * 4;

	public int spawnTime = 0;

	private int lastIndex = 0;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            Initial X coordinate
	 * @param y
	 *            Initial Y coordinate
	 */
	public EntitySpawner(double x, double y) {
		super(x, y, Team.Neutral);

		setStartHealth(20);
		freezeTime = 10;
		spawnTime = random.nextInt(SPAWN_INTERVAL);
		minimapIcon = 4;
		healthBarOffset = 22;
		deathPoints = 0 * 5 + 5;
		yOffs = 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (freezeTime > 0)
			return;

		if (--spawnTime <= 0) {
			spawn();
			spawnTime = Options.difficulty.calculateSpawntime(SPAWN_INTERVAL);
		}
	}

	/**
	 * Spawn a new enemy of the given type onto the field.
	 */
	private void spawn() {
		double x = pos.x + (random.nextFloat() - 0.5) * 5;
		double y = pos.y + (random.nextFloat() - 0.5) * 5;
		x = Math.max(Math.min(x, level.width * Tile.WIDTH), 0);// spawn only
																// inside the
																// level!
		y = Math.max(Math.min(y, level.height * Tile.HEIGHT), 0);
		int xin = (int) x / Tile.WIDTH;
		int yin = (int) y / Tile.HEIGHT;
		Tile spawntile = level.getTile(xin, yin);

		Mob te = getMob(x, y);

		if (level.countEntities(Mob.class) < level.maxMonsters && level.getEntities(te.getBB().grow(8), te.getClass()).isEmpty() && spawntile.canPass(te))
			level.addMob(te, xin, yin);
	}

	protected abstract Mob getMob(double x, double y);

	//TODO: put in correct Artwork
	@Override
	public Bitmap getSprite() {
		int newIndex = (int) (3 - (3 * health) / maxHealth);
		if (newIndex != lastIndex) {
			// if (newIndex > lastIndex) // means more hurt
			// level.addEntity(new SmokeAnimation(pos.x - 12, pos.y - 20,
			// Art.fxSteam24, 40));
			lastIndex = newIndex;
		}
		return Art.entityFiller; //Art.mobSpawner[newIndex][0];
	}

	@Override
	public Bitmap getBitMapForEditor() {
		return Art.entityFiller; //Art.mobSpawner[0][0];
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.draw(Art.entityFiller/*mobSpawnerShadow*/, pos.x - Art.entityFiller/*mobSpawnerShadow*/.getWidth() / 2 - 1, pos.y - Art.entityFiller/*mobSpawnerShadow*/.getHeight() / 2 + 7);
	}

	protected Bitmap drawMobOnTop(Bitmap mobicon) {
		Bitmap bitmap = Art.entityFiller/*mobSpawner[0][0]*/.copy();
		bitmap.draw(mobicon, (bitmap.getWidth() / 2) - mobicon.getWidth() / 2, (bitmap.getHeight() / 2) - 5 - mobicon.getHeight() / 2);
		return bitmap;
	}
}
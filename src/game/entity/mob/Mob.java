package game.entity.mob;

import game.Game;
import game.Options;
import game.entity.Entity;
import game.entity.building.Building;
import game.level.tile.Tile;
import game.math.BB;
import game.math.Vec2;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Screen;

/**
 * Class representing 'thinking' entities. 'Thinking' is AI above the very
 * primitive. Most common subclasses would be Players, Animals, Monsters.
 * 
 * @author Kyle Brodie
 */
public abstract class Mob extends Entity {

	public final static double CARRYSPEEDMOD = 1.2;
	public final static int MoveControlFlag = 1;

	// private double speed = 0.82;
	protected double speed = 1.0;
	protected boolean doShowHealthBar = true;
    protected int healthBarOffset = 10;
	double dir = 0;
	public int hurtTime = 0;
	public int freezeTime = 0;
	public int bounceWallTime = 0;
	public float maxHealth = 10;
	public float health = maxHealth;
	public boolean isImmortal = false;
	public double xBump, yBump;
	public int yOffs = 8;
	public double xSlide;
	public double ySlide;
	public int deathPoints = 0;
	public boolean chasing=false;
	public int justDroppedTicks = 0;
	public int strength = 0;
	public int REGEN_INTERVAL;
	public float REGEN_AMOUNT = 1;
	public boolean REGEN_HEALTH = true;
	public int healingTime = REGEN_INTERVAL;
    protected int facing;
    private int walkTime;
    protected int stepTime;
    protected int limp;
	public boolean isSprint = false;
    public Vec2 aimVector;
	private boolean highlight;
	
	public Mob(double x, double y, int team) {
		super();
		setPos(x, y);
		this.team = team;
		this.REGEN_INTERVAL = Options.difficulty.getRegenerationInterval();
		this.healingTime = this.REGEN_INTERVAL;
		aimVector = new Vec2(0, 1);
	}

	public void setStartHealth(float newHealth) {
		maxHealth = health = newHealth;
	}

	public double getSpeed() {
		return speed;
	}

	public void deltaMove(Vec2 v) {
		super.move(v.x, v.y);
	}

	public int getTeam() {
		return team;
	}

	public boolean isEnemyOf(Mob m) {
		return team != m.team;
	}

	public void tick() {
		if (Options.difficulty.isMobRegenerationAllowed()) {
			this.doRegenTime();
		}
		
		if (hurtTime > 0) {
			hurtTime--;
		}
		
		if (bounceWallTime > 0) {
			bounceWallTime--;
		}
		
		if (freezeTime > 0) {
			slideMove(xSlide, ySlide);
			xSlide *= 0.8;
			ySlide *= 0.8;
			
			if (xBump != 0 || yBump != 0) {
				move(xBump, yBump);
			}
			freezeTime--;
			return;
		} else {
			xSlide = ySlide = 0;
			if (health <= 0) {
				die();
				remove();
				return;
			}
		}
	}
	
	public void doRegenTime() {
		if (!this.REGEN_HEALTH) {
			// DO NOTHING -> REGEN_HEALTH Apply to all health based buff, so prefer REGEN_AMOUNT = 0 for entity that can apply Poison, and Healing potion.
		} else if (hurtTime <= 0 && health < maxHealth && --healingTime <= 0) {
			this.healingTime = this.REGEN_INTERVAL;
			this.onRegenTime();
		}
	}
	
	public void onRegenTime() {
		float regen = this.REGEN_AMOUNT ;
		// Can add thing here like a custom regen action

		this.regenerateHealthBy( regen );
	}
	
	public void regenerateHealthBy(float a) { 
	    health += a ;
	    if (health > maxHealth)
	    	health = maxHealth;
	}
	
	public void slideMove(double xa, double ya) {
		move(xa, ya);
	}

	public void die() {
		int particles = 8;
		//
		// for (int i = 0; i < particles; i++) {
		// double dir = i * Math.PI * 2 / particles;
		// level.addEntity(new Particle(pos.x, pos.y, Math.cos(dir),
		// Math.sin(dir)));
		// }

		if (getDeathPoints() > 0) {
			int loots = 4;
			for (int i = 0; i < loots; i++) {
				double dir = i * Math.PI * 2 / particles;

				//level.addEntity(new Loot(pos.x, pos.y, Math.cos(dir), Math.sin(dir), getDeathPoints()));
			}
		}

		//level.addEntity(new EnemyDieAnimation(pos.x, pos.y));

		//Game.soundPlayer.playSound(getDeathSound(), (float) pos.x, (float) pos.y);
	}

	public String getDeathSound() {
		return "/sound/Explosion.wav";
	}

	public boolean shouldBounceOffWall(double xd, double yd) {
		if (bounceWallTime > 0)
			return false;
		Tile nextTile = level.getTile((int) (pos.x / Tile.WIDTH + Math.signum(xd)), (int) (pos.y / Tile.HEIGHT + Math.signum(yd)));
		boolean re = (nextTile != null && !nextTile.canPass(this));
		if (re)
			bounceWallTime = 10;
		return re;
	}

	public void render(Screen screen) {
		Bitmap image = getSprite();
		if (hurtTime > 0) {
			if (hurtTime > 40 - 6 && hurtTime / 2 % 2 == 0) {
				screen.drawColor(image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2 - yOffs, 0xa0ffffff);
			} else {
				if (health < 0)
					health = 0;
				int col = (int) (180 - health * 180 / maxHealth);
				if (hurtTime < 10)
					col = col * hurtTime / 10;
				screen.drawColor(image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2 - yOffs, (col << 24) + 255 * 65536);
			}
		} else {
					
			screen.draw(image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2 - yOffs);
		}

		//if (doShowHealthBar && health < maxHealth) {
        //    addHealthBar(screen);
        //}
		
		renderMarker(screen);
	}

	/**
	 * Render the marker onto the given screen
	 * 
	 * @param screen
	 *            Screen
	 */
	protected void renderMarker(Screen screen) {
		
		//Don't draw the marker if this doesn't belong to the local team
		//or is not Neutral
		
		//if (!( team == Team.Neutral || team == Game.localTeam )) {
		//	return;
		//}
		
		if (highlight) {
			BB bb = getBB();
			bb = bb.grow((getSprite().getWidth() - (bb.x1 - bb.x0))
					/ (3 + Math.sin(System.currentTimeMillis() * .01)));
			int width = (int) (bb.x1 - bb.x0);
			int height = (int) (bb.y1 - bb.y0);
			Bitmap marker = new Bitmap(width, height);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if ((x < 2 || x > width - 3 || y < 2 || y > height - 3)
							&& (x < 5 || x > width - 6)
							&& (y < 5 || y > height - 6)) {
						int i = x + y * width;
						marker.setPixel(i, 0xffffffff);
					}
				}
			}
			screen.draw(marker, bb.x0, bb.y0 - 4);
		}
	}
	
	/*protected void addHealthBar(Screen s) {
        
        int start = (int) (health * 21 / maxHealth);
        
        float one_tenth_hp = (float) (maxHealth / 10f);
		float three_tenths_hp = one_tenth_hp * 3;
		float size_tenths_hp = one_tenth_hp * 6;
		float eigth_tenths_hp = one_tenth_hp * 8;
		
		int color = 0;
		
		if(health < three_tenths_hp){
			color = 0xf62800;
		}else if (health < size_tenths_hp){
			color = 0xfe7700;
		}else if (health < eigth_tenths_hp){
			color = 0xfef115;
		}else {
			color = 0x8af116;
		}

		s.draw(Art.healthBar_Underlay[0][0], pos.x - 16, pos.y + healthBarOffset);
		s.drawColor(Art.healthBar[start][0], pos.x - 16, pos.y + healthBarOffset, (0xff << 24) + color);
		s.draw(Art.healthBar_Outline[0][0], pos.x - 16, pos.y + healthBarOffset);
    }*/

	public abstract Bitmap getSprite();

	public void hurt(Entity source, float damage) {
		if (isImmortal)
			return;
		
		this.healingTime = this.REGEN_INTERVAL;
		
		/*if (source instanceof Bullet){
			Bullet bullet = (Bullet) source;
			if (bullet.owner instanceof Player){
				Player pl = (Player) bullet.owner;
				if (!isNotFriendOf(pl)){
					return;
				}
			}
		}*/
				
		if (freezeTime <= 0) {
			
			/*if (source instanceof Bullet && !(this instanceof SpawnerEntity) && !(this instanceof RailDroid)) {
				Bullet bullet = (Bullet) source;
				if (bullet.owner instanceof Player) {
					Player pl = (Player) bullet.owner;
					pl.pexp++;
				}
				
				freezeTime = bullet.freezeTime;
			} else {
				freezeTime = 5;
			}*/freezeTime = 5;
			
			hurtTime = 40;
			health -= damage;
			if (health < 0) {
				health = 0;
			}

			double dist = source.pos.dist(pos);
			xBump = (pos.x - source.pos.x) / dist * 2;
			yBump = (pos.y - source.pos.y) / dist * 2;
		}
	}

	@Override
	public void collide(Entity entity, double xa, double ya) {
		xd += xa * 0.4;
		yd += ya * 0.4;
	}

	public int getDeathPoints() {
		return deathPoints;
	}
    
    public boolean isTargetBehindWall(double dx2, double dy2, Entity e) {
        int x1 = (int) pos.x / Tile.WIDTH;
        int y1 = (int) pos.y / Tile.HEIGHT;
        int x2 = (int) dx2 / Tile.WIDTH;
        int y2 = (int) dy2 / Tile.HEIGHT;

        int dx, dy, inx, iny, a;
        Tile temp;
        Tile dTile1;
        Tile dTile2;
        dx = x2 - x1;
        dy = y2 - y1;
        inx = dx > 0 ? 1 : -1;
        iny = dy > 0 ? 1 : -1;

        dx = java.lang.Math.abs(dx);
        dy = java.lang.Math.abs(dy);

        if (dx >= dy) {
            dy <<= 1;
            a = dy - dx;
            dx <<= 1;
            while (x1 != x2) {
                temp = level.getTile(x1, y1);
                if (!temp.canPass(e)) {
                    return true;
                }
                if (a >= 0) {
                	dTile1=level.getTile(x1+inx,y1);
                	dTile2=level.getTile(x1,y1+iny);
                	if (!(dTile1.canPass(e)||dTile2.canPass(e))){
                		return true;
                	}
                    y1 += iny;
                    a -= dx;
                }
                a += dy;
                x1 += inx;
            }
        } else {
            dx <<= 1;
            a = dx - dy;
            dy <<= 1;
            while (y1 != y2) {
                temp = level.getTile(x1, y1);
                if (!temp.canPass(e)) {
                    return true;
                }
                if (a >= 0) {
                	dTile1=level.getTile(x1+inx,y1);
                	dTile2=level.getTile(x1,y1+iny);
                	if (!(dTile1.canPass(e)||dTile2.canPass(e))){
                		return true;
                	}
                	x1 += inx;
                    a -= dy;
                }
                a += dx;
                y1 += iny;
            }
        }
        temp = level.getTile(x1, y1);
        if (!temp.canPass(e)) {
            return true;
        }
        return false;
    }
    
    /*public boolean fallDownHole() {
        int x=(int)(pos.x/Tile.WIDTH);
        int y=(int)(pos.y/Tile.HEIGHT);
        if (level.getTile(x, y) instanceof HoleTile) {
            if (!(this instanceof Player)){
                ItemFallAnimation animation = new ItemFallAnimation(x*Tile.WIDTH, y*Tile.HEIGHT, this.getSprite());
                if(this instanceof Harvester){
                    animation.setHarvester();
                }
                level.addEntity(animation);
                remove();
            } else {
                GameCharacter character = ((Player)this).getCharacter();
                level.addEntity(new PlayerFallingAnimation(x*Tile.WIDTH, y*Tile.HEIGHT, character));
                if (((Player)this).isCarrying()){
                    ItemFallAnimation animation = new ItemFallAnimation(x*Tile.WIDTH, (y-1)*Tile.HEIGHT, ((Player)this).carrying.getSprite());
                    if(((Player)this).carrying instanceof Harvester){
                        animation.setHarvester();
                    }
                    level.addEntity(animation);
                    ((Player)this).carrying.remove();
                }
                // TODO add a sex attribute to Characters
                if (character.ordinal() < 2)
                    Game.soundPlayer.playSound("/sound/falling_male.wav", (float) pos.x, (float) pos.y);
                else
                    Game.soundPlayer.playSound("/sound/falling_female.wav", (float) pos.x, (float) pos.y);
            }
            return true;
        }
        return false;
    }*/
    
    public void walk(){
    	switch (facing) {
        case 0:
            yd -= speed;
            break;
        case 1:
            xd += speed;
            break;
        case 2:
            yd += speed;
            break;
        case 3:
            xd -= speed;
            break;
    	}
    	walkTime++;

    	if (walkTime / 12 % limp != 0) {
    		if (shouldBounceOffWall(xd, yd)) {
    			facing = (facing + 2) % 4;
    			xd = -xd;
    			yd = -yd;
    		}

    		stepTime++;
    		if ((!move(xd, yd) || (walkTime > 10 && random.nextInt(200) == 0) && chasing==false)) {
    			facing = random.nextInt(4);
    			walkTime = 0;
    		}
    	}
    	xd *= 0.2;
    	yd *= 0.2;
    }
    
    /**
     * Get current player position
     * 
     * @return Position
     */
    public Vec2 getPosition() {
        return pos;
    }

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
}
package game.level;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.entity.*;
import game.gfx.Screen;
import game.level.tile.Tile;

public class Level {
	
	public Player player;
	private Tile[][] map;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	public int width,height;
	
	public Level(Game g) {
		width = g.GAME_WIDTH;
		height = g.GAME_HEIGHT;
		map = new Tile[width/16][height/16];
	}

	public void add(Entity entity) {
		if(entity instanceof Player)
			player = (Player) entity;
		entity.removed = false;
		entities.add(entity);
		entity.init(this);
	}

	public void tick() {
		entities.removeAll(entitiesToRemove);
		entitiesToRemove.clear();
		for(int i = 0; i < entities.size(); i++)
			entities.get(i).tick();
	}

	public void render(Screen screen) {
		for(int i = 0; i < entities.size(); i++)
			entities.get(i).render(screen);
	}

	public void remove(Entity e) {
		entitiesToRemove.add(e);
	}
}
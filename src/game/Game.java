package game;

import game.entity.TestEntity;
import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main class that runs the game.
 * 
 * @author Kyle Brodie
 *
 */
public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "Om Nom Zom";
	public static final int GAME_WIDTH = 10;
	public static final int GAME_HEIGHT = 10;
	public static final int SCREEN_WIDTH = GAME_WIDTH * Tile.WIDTH;
	public static final int SCREEN_HEIGHT = GAME_HEIGHT * Tile.HEIGHT;
	public static final int SCALE = 2;
	public static final int TICKS_PER_SEC = 60;
	
	private boolean running = false;
	public long totalTicks,totalGameTicks;

	private Screen screen;
	
	private Keys keys = new Keys();
	private Level level;
	
	private void init() {
        running = true;
        screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT);
        level = new Level(GAME_WIDTH,GAME_HEIGHT);
        level.addEntity(new TestEntity(0,0,keys));
        
        createBufferStrategy(2);
        requestFocus();
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double nsPerTick = 1000000000.0 / TICKS_PER_SEC;
		double unprocessedTicks = 0;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			unprocessedTicks += (now - lastTime) / nsPerTick;
			lastTime = now;

			while (unprocessedTicks >= 1) {
				ticks++;
				tick();
				unprocessedTicks -= 1;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(2);
				continue;
			}
			Graphics g = bs.getDrawGraphics();
			
			render(g);
			frames++;
			
			if(bs != null) {
				bs.show();
			}
			
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		totalTicks++;
		if (!hasFocus()) {
			keys.release();
		} else {
			totalGameTicks++;

			keys.tick();
			level.tick();
		}
	}
	
	private void render(Graphics g) {
		if (!hasFocus()) {
			g.drawString("PAUSED!", SCREEN_WIDTH * SCALE / 2, SCREEN_HEIGHT * SCALE / 2);
			return;
		}
		
		screen.clear();
		level.render(screen);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.drawImage(screen.image, 0, 0, SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE, null);
	}
	
	public Game() {
            this.setPreferredSize(new Dimension(SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE));
            this.setMinimumSize(new Dimension(SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE));
            this.setMaximumSize(new Dimension(SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE));
            this.addKeyListener(new KeyboardHandler(keys));
            JFrame frame = new JFrame();
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(this);
            frame.setContentPane(panel);
            frame.pack();
            frame.setResizable(false);
            frame.setIgnoreRepaint(true);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		g.start();
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
}
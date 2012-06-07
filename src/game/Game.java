package game;

import game.entity.mob.Player;
import game.gfx.Screen;
import game.level.Level;
import game.level.tile.WallTile;
import game.math.Vector2d;
import game.math.Vector2i;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

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
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;
	public static final int SCALE = 2;
	public static final int TICKS_PER_SEC = 60;
	
	private boolean running = false;
	public boolean paused = false;
	public long totalTicks,totalGameTicks;

	private Screen screen;
	
	private Keys keys = new Keys();
	private Level level;
	private Player player;
	
	private void init() {
        running = true;
        screen = new Screen(GAME_WIDTH, GAME_HEIGHT);
        level = new Level(64,64);
        player = new Player(0,0,keys);
        level.addEntity(player);
        level.setTile(10, 10, new WallTile());
        level.setTile(10, 11, new WallTile());
        level.setTile(10, 12, new WallTile());
        level.setTile(10, 13, new WallTile());
        level.setTile(10, 14, new WallTile());
        
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
			paused = true;
		} else {
			paused = false;
			if (!paused) {
				totalGameTicks++;

				keys.tick();
				level.tick();
			}
		}
	}
	
	private void render(Graphics g) {
		if (paused) {
			g.drawString("PAUSED!", GAME_WIDTH * SCALE / 2, GAME_HEIGHT * SCALE / 2);
			return;
		}
		
		if(level != null) {
			Vector2d pos = player.getDrawPos();
			int xScroll = (int) (pos.x - screen.getWidth() / 2);
			int yScroll = (int) (pos.y - (screen.getHeight() - 24) / 2);
			level.render(screen, xScroll, yScroll);
		}

		g.setColor(Color.BLACK);

		g.fillRect(0, 0, getWidth(), getHeight());
		g.translate((getWidth() - GAME_WIDTH * SCALE) / 2, (getHeight() - GAME_HEIGHT * SCALE) / 2);
		g.clipRect(0, 0, GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE);

		BufferedImage image = optimizeImage(screen.image);
		g.drawImage(image, 0, 0, GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE, null);
		
		Vector2i pos = player.getPos();
		g.setColor(Color.white);
		g.drawString("Player Tile X: " + pos.x, GAME_WIDTH * SCALE - 150, 3 * 40);
		g.drawString("Player Tile Y: " + pos.y, GAME_WIDTH * SCALE - 150, 4 * 40);
	}
	
	private BufferedImage optimizeImage(BufferedImage image)
	{
	        // obtain the current system graphical settings
	        GraphicsConfiguration gfx_config = GraphicsEnvironment.
	                getLocalGraphicsEnvironment().getDefaultScreenDevice().
	                getDefaultConfiguration();

	        /*
	         * if image is already compatible and optimized for current system 
	         * settings, simply return it
	         */
	        if (image.getColorModel().equals(gfx_config.getColorModel()))
	                return image;

	        // image is not optimized, so create a new image that is
	        BufferedImage new_image = gfx_config.createCompatibleImage(
	                        image.getWidth(), image.getHeight(), image.getTransparency());

	        // get the graphics context of the new image to draw the old image on
	        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

	        // actually draw the image and dispose of context no longer needed
	        g2d.drawImage(image, 0, 0, null);
	        g2d.dispose();

	        // return the new optimized image
	        return new_image; 
	}
	
	public Game() {
            this.setPreferredSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
            this.setMinimumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
            this.setMaximumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
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
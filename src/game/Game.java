package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;


import game.level.*;
import game.entity.*;
import game.gfx.Screen;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "OmNomZom";
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;
	public static final int SCALE = 2;
	public static final int TICKS_PER_SEC = 60;
	
	private boolean running = false;
	public long totalTicks,totalGameTicks;

	private Screen screen = new Screen(GAME_WIDTH,GAME_HEIGHT);
	private Keys keys = new Keys();
	private Level level;
	public static Object soundPlayer;
	
	private static volatile File tempDir = null;
	
	private void init() {
            running = true;
            level = new Level(this);
            level.add(new Player(keys));
            
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
			} catch (InterruptedException e) { e.printStackTrace(); }

			frames++;
                        BufferStrategy bs = getBufferStrategy();
                        if(bs == null) {
                            createBufferStrategy(2);
                            continue;
                        }
                        Graphics g = bs.getDrawGraphics();
			render(g);
			
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
			if (!level.player.removed) totalGameTicks++;

			keys.tick();
			level.tick();
		}
	}
	
	private static boolean drewPaused = false;
	
	private void render(Graphics g) {
            if(!this.hasFocus()) {
                if(!drewPaused) {
                    g.drawString("PAUSED!", GAME_WIDTH * SCALE / 2, GAME_HEIGHT * SCALE / 2);
                    drewPaused = true;
		}
		return;
            }
            drewPaused = false;
		
            level.render(screen);
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.drawImage(screen.image, 0, 0, GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE, null);
	}
	
	public Game() {
            this.setPreferredSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
            this.setMinimumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
            this.setMaximumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
            this.addKeyListener(new InputHandler(keys));
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

	public static File getTempDir() {
		if (tempDir == null) {
		tempDir = getAppDir("omnomzom");
		}
		return tempDir;
		}

	public static File getAppDir(String s) {
		String s1 = System.getProperty("user.home", ".");
		File file;
		String sys = getOS();
		
		if (sys.contains("win")) {
			String s2 = System.getenv("APPDATA");
			if (s2 != null) {
				file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
			} else {
				file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
			}
		} else if (s.contains("mac")) {
			file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
		} else if (s.contains("solaris")) {
			file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
		} else if (s.contains("linux")) {
			file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
		} else {
			file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
		}
		if (!file.exists() && !file.mkdirs()) {
			throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
		} else {
			return file;
		}
	}
	
	public static String getOS() {
		return System.getProperty("os.name").toLowerCase();;
	}
}
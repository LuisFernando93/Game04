package br.com.inarigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.inarigames.entities.Entity;
import br.com.inarigames.entities.Player;
import br.com.inarigames.graphics.GameOver;
import br.com.inarigames.graphics.Spritesheet;
import br.com.inarigames.graphics.Start;
import br.com.inarigames.graphics.UI;

public class Game extends Canvas implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	private double amountOfUpdates = 60.0;
	public static Random random;
	
	private BufferedImage image;
	
	public static Player player;
	public static List<Entity> entities;
	public static List<Entity> toRemove;
	public static Spritesheet spritesheet =  new Spritesheet("/spritesheet.png");
	
	public static final int WIDTH = 384;
	public static final int HEIGHT = 256;
	public static final int SCALE = 2;
	
	private static String gameState = "START";
	
	private Start start;
	private GameOver gameOver;
	private UI ui;
	
	public Game() {
		
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		//initializing objects
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		player = new Player(176, 128, 32, 32, Game.spritesheet.getSprite(0, 0, 32, 32));
		entities.add(player);
		random = new Random();
		ui = new UI();
		start = new Start();
	}
	
	public static void setGameState(String gameState) {
		Game.gameState = gameState;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	private void initFrame() {
		
		frame = new JFrame("Game #4");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	private synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	private synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("erro na thread");
			e.printStackTrace();
		}
	}
	
	public static void newGame() {
		entities.clear();
	}
	
	private void update() {
		switch (Game.gameState) {
		
		case "START":
			start.update();
			break;
		
		case "NORMAL":
			
			Collections.sort(entities, Entity.entitySorter);
			for (Entity entity : entities) {
				entity.update();
			}
			entities.removeAll(toRemove);
			toRemove.clear();
			break;
			
		case "GAME OVER":
			gameOver.update();
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + Game.gameState);
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(122,102,255));
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (Entity entity : entities) {
			entity.render(graphics);
		}
		
		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		ui.render(graphics);
		
		switch (Game.gameState) {
		
		case "START":
			start.render(graphics);
			break;
		
		case "GAME OVER":
			gameOver.render(graphics);
			break;
			
		}
			
		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / amountOfUpdates;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS = " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (Game.gameState) {
		
		case "START":
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				start.setStart(true);
			}
			break;
		
		case "NORMAL":
			
			break;
		
		case "GAME OVER":
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameOver.setRestart(true);
			}
			break;
			
		}
		
	}

}
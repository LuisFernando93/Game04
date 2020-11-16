package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.com.inarigames.main.Game;

public class World {
	
	private static Tile[][] tiles;
	private static int WIDTH, HEIGHT;
	public final static int TILE_SIZE = 32;
	
	public World(String path) {
		
		createWorld(path);
		
	}
	
	public static Tile[][] getTiles() {
		return World.tiles;
	}
	
	public static int getWidth() {
		return World.WIDTH;
	}
	
	public static int getHeight() {
		return World.HEIGHT;
	}
	
	public void createWorld(String path) {
		try {
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH][HEIGHT];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, WIDTH);
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					int pixelAtual = pixels[i + (j*WIDTH)];
					tiles[i][j] = new BackgroundTile(i*TILE_SIZE, j*TILE_SIZE, Tile.TILE_BACKGROUND);
					switch (pixelAtual) {
					case 0xFF000000:
						//preto - background
						break;
						
					case 0xFFFFFFFF:
						//branco - block
						tiles[i][j] = new BlockTile(i*TILE_SIZE, j*TILE_SIZE, Tile.TILE_BLOCK);
						if ((j-1) >= 0 && pixels[i + ((j-1)*map.getWidth())] == 0xFFFFFFFF) {
							tiles[i][j] = new BlockTile(i*TILE_SIZE, j*TILE_SIZE, Tile.TILE_DIRT);
						}
						break;
						
					case 0xFF0000FF:
						//azul - player
						Game.player.setX(i*TILE_SIZE);
						Game.player.setY(j*TILE_SIZE);
						break;

					default:
						throw new IllegalArgumentException("Unexpected value: " + pixelAtual);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+height-1) / TILE_SIZE;
		
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
		
		boolean isFree = !(tiles[x1][y1] instanceof BlockTile || 
						tiles[x2][y2] instanceof BlockTile || 
						tiles[x3][y3] instanceof BlockTile || 
						tiles[x4][y4] instanceof BlockTile);
		
		return isFree;
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		try {
			boolean isFree = !(tiles[x1][y1] instanceof BlockTile || 
					tiles[x2][y2] instanceof BlockTile || 
					tiles[x3][y3] instanceof BlockTile || 
					tiles[x4][y4] instanceof BlockTile);
	
			return isFree;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Player out of bound");
			return true;
		}
		
	}
	
	public void render(Graphics graphics) {
		
		int xstart = Camera.getX()/TILE_SIZE;
		int ystart = Camera.getY()/TILE_SIZE;
		
		int xfinal = xstart + (Game.WIDTH/TILE_SIZE);
		int yfinal = ystart + (Game.HEIGHT/TILE_SIZE);
		
		for (int i = xstart; i <= xfinal; i++) {
			for(int j = ystart; j <= yfinal; j++) {
				if(i < 0 || j < 0 || i >= WIDTH || j >= HEIGHT)
					continue;
				Tile tile = tiles[i][j];
				tile.render(graphics);
			}
		}
	}
	
}

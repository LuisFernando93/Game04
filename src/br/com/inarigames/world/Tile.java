package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;

public class Tile {

	public static BufferedImage TILE_BACKGROUND = Game.spritesheet.getSprite(0, 0, 32, 32);
	public static BufferedImage TILE_BLOCK = Game.spritesheet.getSprite(32, 0, 32, 32);
	public static BufferedImage TILE_DIRT = Game.spritesheet.getSprite(32, 32, 32, 32);
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, Camera.offsetX(x) , Camera.offsetY(y), null);
	}
}

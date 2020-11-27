package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;

public class Coin extends Entity {
	
	private int frames = 0, maxFrames = 10, imageIndex = 0, maxIndex = 2;
	private BufferedImage[] coinSprites;

	public Coin(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.maskx = 4;
		this.masky = 4;
		this.maskw = 24;
		this.maskh = 24;
		coinSprites = new BufferedImage[2];
		for (int i = 0; i < coinSprites.length; i++) {
			coinSprites[i] = Game.spritesheet.getSprite(32*i, 2*32, 32, 32);
		}
	}
	
	private void flipCoin() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			imageIndex++;
			if (imageIndex == maxIndex) {
				imageIndex = 0;
			}
		}
	}
		
	public void update() {
		flipCoin();
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(coinSprites[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
	}
		
	
}

package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Entity {
	
	protected double x, y;
	protected BufferedImage sprite;
	protected int width, height;
	protected int maskx, masky, maskw, maskh;
	protected int depth;
	protected static int GRAVITY = 4;
	
	protected static BufferedImage PLAYER_RIGHT_EN = Game.spritesheet.getSprite(2*32, 0, 32, 32);
	protected static BufferedImage PLAYER_LEFT_EN = Game.spritesheet.getSprite(2*32, 32, 32, 32);
	protected static BufferedImage PLAYER_JUMP_RIGHT_EN = Game.spritesheet.getSprite(6*32, 0, 32, 32);
	protected static BufferedImage PLAYER_JUMP_LEFT_EN = Game.spritesheet.getSprite(6*32, 32, 32, 32);
	
	public static Comparator<Entity> entitySorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity e0, Entity e1) {
			if (e0.getDepth() < e1.getDepth())
				return -1;
			if (e0.getDepth() > e1.getDepth())
				return +1;
			return 0;
		}
	};
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		maskx = this.getX();
		masky = this.getY();
		maskw = width;
		maskh = height;
		depth = 1;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.maskx, e1.masky, e1.maskw, e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.maskx, e2.masky, e2.maskw, e2.maskh);
		if (e1Mask.intersects(e2Mask)) {
			return true;
		}
		return false;
	}
	
	protected void freeFall() {
		if (World.isFree(this.getX(), this.getY() + GRAVITY)) {
			this.y+=GRAVITY;
		}
	}
	
	public void update() {
		
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
	}
}

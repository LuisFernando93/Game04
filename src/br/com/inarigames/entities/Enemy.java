package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.World;

public class Enemy extends Entity{
	
	private int speed = 1;
	private int life = 1;
	private boolean right = true, left = false;
	
	public void takeDamage(int power) {
		this.life -= power;
	}

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.depth = 1;
		this.maskx = 6;
		this.masky = 9;
		this.maskw = 20;
		this.maskh = 23;
		
	}	
	
	private void move() {
		if (right) {
			if (World.isFree(this.getX() + speed, this.getY())) {
				this.x += speed;
				if (World.isFree(this.getX() + World.TILE_SIZE, this.getY() + 1)) {
					right = false;
					left = true;
				}
			} else {
				right = false;
				left = true;
			}
		} 
			
		if (left) {
			if (World.isFree(this.getX() - speed, this.getY())) {
				this.x -= speed;
				if (World.isFree(this.getX() - World.TILE_SIZE, this.getY() + 1)) {
					right = true;
					left = false;
				}
			} else {
				right = true;
				left = false;
			}
		} 
	}

	private void checkLife() {
		if (life <= 0) {
			Game.toRemove.add(this);
			return;
		}
	}
	
	public void update() {
		freeFall();
		move();
		checkLife();
	}
	
	public void render(Graphics graphics) {
		if(right) {
			sprite = ENEMY_RIGHT_EN;
		} else if(left) {
			sprite = ENEMY_LEFT_EN;
		}
		super.render(graphics);
	}
}

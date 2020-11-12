package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity{
	
	private boolean right = false, left = false;
	private boolean jump = false;
	private int speed = 2;
	private int dir;
	private int right_dir = 1, left_dir = 2;
	

	public Player(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 2;
		dir = right_dir;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	private void move() {
		if (right && World.isFree(this.getX()+speed, this.getY())) {
			this.x+=speed;
			dir = right_dir;
		} else if (left && World.isFree(this.getX()-speed, this.getY())) {
			this.x-=speed;
			dir = left_dir;
		}
	}
	
	public void update() {
		freeFall();
		move();
	}
	
	public void render(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(PLAYER_RIGHT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(PLAYER_LEFT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
	}

}

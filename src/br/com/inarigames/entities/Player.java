package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity{
	
	private boolean right = false, left = false;
	
	private boolean jump = false;
	private boolean isJumping = false;
	private boolean isMoving = false;
	private int jumpSpeed = 4;
	private int jumpFrames = 0;
	private int jumpHeight = 80;
	
	private BufferedImage[] playerMoveRightSprites;
	private BufferedImage[] playerMoveLeftSprites;
	private int frames = 0, maxFrames = 10, imageIndex = 0, maxIndex = 3; 
	
	private int speed = 2;
	private int dir;
	private int right_dir = 1, left_dir = 2;
	

	public Player(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 2;
		dir = right_dir;
		playerMoveRightSprites = new BufferedImage[3];
		playerMoveLeftSprites = new BufferedImage[3];
		for (int i = 0; i < playerMoveRightSprites.length; i++) {
			playerMoveRightSprites[i] = Game.spritesheet.getSprite(32*(i+3), 0, 32, 32);
			playerMoveLeftSprites[i] = Game.spritesheet.getSprite(32*(i+3), 32, 32, 32);
		}
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
	
	private void playerMovementAnimation() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			imageIndex++;
			if (imageIndex == maxIndex) {
				imageIndex = 0;
			}
		}
	}
	
	private void move() {
		if (right && World.isFree(this.getX()+speed, this.getY())) {
			this.x+=speed;
			dir = right_dir;
			playerMovementAnimation();
			isMoving = true;
		} else if (left && World.isFree(this.getX()-speed, this.getY())) {
			this.x-=speed;
			dir = left_dir;
			playerMovementAnimation();
			isMoving = true;
		} else {
			isMoving = false;
		}
	}
	
	private void jump() {
		
		if (jump) {
			if (!World.isFree(this.getX(), this.getY() + 1)) {
				isJumping = true;
			}
		}
		
		if (isJumping) {
			if (World.isFree(this.getX(), this.getY()-jumpSpeed)) {
				y-=jumpSpeed;
				jumpFrames+=jumpSpeed;
				if (jumpFrames >= jumpHeight) {
					isJumping = false;
					jump = false;
					jumpFrames = 0;
				}
			} else {
				isJumping = false;
				jump = false;
				jumpFrames = 0;
			}
		} 
		
	}
	
	protected void freeFall() {
		if (World.isFree(this.getX(), this.getY() + GRAVITY)  && !isJumping) {
			this.y+=GRAVITY;
		}
	}
	
	private void updateCamera() {
		int xx = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.getWidth()*World.TILE_SIZE - Game.WIDTH);
		int yy = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.getHeight()*World.TILE_SIZE -  Game.HEIGHT);
		Camera.setX(xx);
		Camera.setY(yy);
	}
	
	public void update() {
		freeFall();
		move();
		jump();
		updateCamera();
	}
	
	private void movementRender(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(playerMoveRightSprites[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(playerMoveLeftSprites[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
	}
	
	private void staticRender(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(PLAYER_RIGHT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(PLAYER_LEFT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
	}
	
	public void render(Graphics graphics) {
		if (isMoving) {
			movementRender(graphics);
		} else {
			staticRender(graphics);
		}
	}

}

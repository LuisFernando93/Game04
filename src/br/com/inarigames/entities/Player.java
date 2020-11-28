package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity{
	
	private boolean right = false, left = false;
	
	private boolean jump = false;
	private boolean run = false;
	private boolean isMoving = false;
	private boolean isDamaged = false;
	private int jumpHeight = 11;
	
	private BufferedImage[] playerMoveRightSprites;
	private BufferedImage[] playerMoveLeftSprites;
	private int frames = 0, maxFrames = 10, imageIndex = 0, maxIndex = 3; 
	private int damageFrames = 0, maxDamageFrames = 12;
	
	private static final int MAX_LIFE = 3;
	private int life = MAX_LIFE;
	private int walkSpeed = 3;
	private int runSpeed = 6;
	private int speed = walkSpeed;
	private int power = 1;
	private int dir;
	private int right_dir = 1, left_dir = 2;
	

	public Player(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 2;
		dir = right_dir;
		this.maskx = 7;
		this.masky = 0;
		this.maskw = 18;
		this.maskh = 32;
		playerMoveRightSprites = new BufferedImage[3];
		playerMoveLeftSprites = new BufferedImage[3];
		for (int i = 0; i < playerMoveRightSprites.length; i++) {
			playerMoveRightSprites[i] = Game.spritesheet.getSprite(32*(i+3), 0, 32, 32);
			playerMoveLeftSprites[i] = Game.spritesheet.getSprite(32*(i+3), 32, 32, 32);
		}
	}
	
	public int getLife() {
		return this.life;
	}
	
	public int getMaxLife() {
		return Player.MAX_LIFE;
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
	
	public void setRun(boolean run) {
		this.run = run;
	}
	
	public void takeDamege(int power) {
		this.life-=power;
		if(this.life < 0) {
			this.life = 0;
		}
	}
	
	public void checkRun() {
		if (run) {
			this.speed = runSpeed;
		} else this.speed = walkSpeed;
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
		
		if(!World.isFree(this.getX(), this.getY() + 1) && jump) {
			this.vspd = -(jumpHeight);
			jump = false;
		}
		
	}
	
	private void halfJump() {
		
		this.vspd = (-3*jumpHeight)/4;
		System.out.println(vspd);
	}
	
	private Enemy collidingEnemy() {
		for (Entity entity : Game.entities) {
			if (entity instanceof Enemy) {
				if (isColliding(this, entity)) {
					return (Enemy)entity;
				}
			}
		}
		return null;
	}
	
	private Entity collidingItem() {
		for (Entity entity : Game.entities) {
			if (entity instanceof Coin) {
				if(isColliding(this, entity)) {
					return entity;
				}
			}
		}
		return null;
	}
	
	private void stompEnemy(Enemy enemy) {
		enemy.takeDamage(power);
		halfJump();
	}
	
	protected void freeFall() {
		
		vspd+=GRAVITY;
		jump();
		
		if(!World.isFree(this.getX(), (int)(this.y + vspd))) {
			int signVsp = 0;
			if (vspd >= 0) {
				signVsp = 1;
			} else {
				signVsp = -1;
			}
			
			while (World.isFree(this.getX(), (int)(this.y + signVsp) )) {
				y+=signVsp; 
			}
			vspd = 0;
		}
		y += vspd;
		
	}
	
	private void checkIfStomp() {
		if(World.isFree(this.getX(), this.getY() + 1)) {
			freeFalling = true;
		} else freeFalling = false;
		
		if(freeFalling) {
			Enemy enemy = collidingEnemy();
			if (enemy != null) {
				stompEnemy(enemy);
			}
		}
	}
	
	private void updateCamera() {
		int xx = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.getWidth()*World.TILE_SIZE - Game.WIDTH);
		int yy = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.getHeight()*World.TILE_SIZE -  Game.HEIGHT);
		Camera.setX(xx);
		Camera.setY(yy);
	}
	
	private void fallOutOfGame() {
		if (this.y >= Game.HEIGHT + 2*this.height) {
			this.life = 0;
		}
	}
	
	private void takeItem() {
		Entity item = collidingItem();
		if (item != null) {
			//item coletado. verificar tipo
			if (item instanceof Coin) {
				Game.increaseScore();
				Game.toRemove.add(item);
			}
		}
	}
	
	private void takeDamage() {
		Enemy enemy = collidingEnemy();
		if(enemy != null && !freeFalling && !isDamaged) {
			life-=enemy.getPower();
			isDamaged = true;
		}
	}
	
	private void checkIfIsDamaged() {
		
		if (isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == maxDamageFrames){
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
	}
	
	private void checkLife() {
		if (this.life <= 0) {
			Game.toRemove.add(this);
			Game.setGameState("GAME OVER");
		}
	}
	
	private void checkGoal() {
		for (Entity entity : Game.entities) {
			if (entity instanceof Goal) {
				if(isColliding(this, entity)) {
					Game.endLevel();
				}
			}
		}
	}
	
	public void update() {
		checkIfStomp();
		freeFall();
		checkRun();
		move();
		updateCamera();
		fallOutOfGame();
		takeItem();
		takeDamage();
		checkIfIsDamaged();
		checkLife();
		checkGoal();
	}
	
	private void jumpRender(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(PLAYER_JUMP_RIGHT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(PLAYER_JUMP_LEFT_EN, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
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
		if (vspd<0) {
			jumpRender(graphics);
		} else if (isMoving) {
			movementRender(graphics);
		} else {
			staticRender(graphics);
		}
	}

}

package br.com.inarigames.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import br.com.inarigames.main.Game;

public class Start {
	
	private boolean start = false;
	private boolean showMessageStart = true;
	private int framesStart = 0;
	private final int MAX_FRAMES_START = 30;

	public void setStart(boolean start) {
		this.start = start;
	}
	
	public void update() {
		this.framesStart++;
		if(this.framesStart == MAX_FRAMES_START) {
			this.framesStart = 0;
			if (this.showMessageStart) 
				this.showMessageStart = false;
			  else
				this.showMessageStart = true;
		}
		
		if(start) {
			this.start = false;
			Game.setGameState("NORMAL");
		}
	}

	public void render(Graphics graphics) {
		Graphics2D graphics2 = (Graphics2D) graphics;
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics2.setColor(new Color(0, 0, 0, 100));
		graphics2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		graphics.setColor(Color.white);
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics.drawString("Game 04", (Game.WIDTH*Game.SCALE)/2 - 60, (Game.HEIGHT*Game.SCALE)/2 - 60);
		graphics.setFont(new Font("arial", Font.BOLD, 25));
		if (showMessageStart) {
			graphics.drawString(">Pressione Enter para começar<", (Game.WIDTH*Game.SCALE)/2 - 180, (Game.HEIGHT*Game.SCALE)/2 - 10);
		}
	}
}

package br.com.inarigames.graphics;

import java.awt.Color;
import java.awt.Graphics;

import br.com.inarigames.main.Game;

public class UI {
	
	private int life;
	private int maxLife;
	
	public void render(Graphics graphics) {
		graphics.setColor(Color.RED);
		maxLife = Game.player.getMaxLife();
		for (int i = 0; i < maxLife; i++) {
			graphics.fillOval(20 + 40*i, 20, 30, 30);
		}
		life = Game.player.getLife();
		graphics.setColor(Color.GREEN);
		for (int i = 0; i < life; i++) {
			graphics.fillOval(20 + 40*i, 20, 30, 30);
		}
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < 3; i++) {
			graphics.drawOval(20 + 40*i, 20, 30, 30);
		}
	}
}

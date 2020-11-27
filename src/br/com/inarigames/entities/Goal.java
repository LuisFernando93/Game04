package br.com.inarigames.entities;

import java.awt.image.BufferedImage;

public class Goal extends Entity {

	public Goal(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.maskx = 15;
		this.masky = 2;
		this.maskh = 30;
		this.maskw = 16;
	}

}

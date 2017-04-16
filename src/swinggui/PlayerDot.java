package swinggui;

import java.awt.Color;
import java.awt.Graphics;

import player.Player;

public class PlayerDot {
	private int x, y;
	private Color color;
	
	public PlayerDot(Player player) {
		this.x = player.getRow();
		this.y = player.getColumn();
		this.color = player.getColor();
	}
	
	public void draw(Graphics g, int size, int margin) {
		g.setColor(color);
		g.fillOval(x*size, y*size, size - margin, size - margin);
	}
}

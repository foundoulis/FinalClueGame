package swinggui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTextField;

import clueGame.BoardCell;
import clueGame.DoorDirection;

public class BoxGUI {
	private Color color, borderColor;
	private int x, y;
	private String door;
	private BoardCell boardCell;
	private int size;
	
	public BoxGUI(BoardCell b) {
		this.x = b.getRow();
		this.y = b.getColumn();
		this.boardCell = b;
	}
	
	public void draw(Graphics g, int size, int margin) {
		this.size = size;
		draw(g, size, margin, false);
	}
	
	public void draw(Graphics g, int size, int margin, boolean isTarget) {
		if (isTarget) {
			g.setColor(Color.CYAN);
			g.fillRect(x*size, y*size, size, size);
		} else if (boardCell.isWalkway()) {
			g.setColor(Color.WHITE);
			g.fillRect(x*size, y*size, size, size);
			g.setColor(Color.GREEN);
			g.fillRect(x*size + margin, y*size + margin, size - margin*2, size - margin*2);
		} else {
			g.setColor(Color.GRAY);
			g.fillRect(x*size, y*size, size, size);
		}

		if (boardCell.isDoorway()) {
			g.setColor(Color.MAGENTA);
			switch (boardCell.getDoorDirection()) {
			case UP:
				g.fillRect(x*size, y*size, size, size/2);
				break;
			case DOWN:
				g.fillRect(x*size, y*size + size/2, size, size/2);
				break;
			case LEFT:
				g.fillRect(x*size, y*size, size/2, size);
				break;
			case RIGHT:
				g.fillRect(x*size + size/2, y*size, size/2, size);
				break;
			default:
				g.fillRect(x*size, y*size, size, size);
				break;
			}
		}
	}
	
	public boolean containsClick(Point p) {
		Rectangle rect = new Rectangle(x*size, y*size, size, size);
		return rect.contains(p);
	}

	public Component toComponent() {
		JTextField cell = new JTextField();
		cell.setText(door);
		cell.setBackground(this.color);	
		cell.setEditable(false);
		return cell;
	}

}

package swinggui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTextField;

import clueGame.BoardCell;
import clueGame.DoorDirection;

public class BoxGUI {

	public static final int BOX_WIDTH = 30;
	public static final int BOX_HEIGHT = 30;
	public static final int BOX_MARGIN = 0;
	
	private Color color;
	
	private int x, y;
	
	private String door;
	
	public BoxGUI(BoardCell b) {
		this.x = b.getColumn();
		this.y = b.getRow();
		this.setColor(b);
	}
	
	private void setColor(BoardCell b) {
		if (b.isDoorway()) {
			this.color = Color.WHITE;
			switch (b.getDoorDirection()) {
			case UP: door = "^"; break;
			case DOWN: door = "\\/"; break;
			case LEFT: door = "<"; break;
			case RIGHT: door = ">"; break;
			default:
				door = "!";
				break;
			}
		} else if (b.isRoom()) {
			this.color = Color.GRAY;
		} else if (b.isWalkway()) {
			this.color = Color.GREEN;
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, BOX_WIDTH, BOX_HEIGHT);
	}

	public Component toComponent() {
		JTextField cell = new JTextField();
		cell.setText(door);
		cell.setBackground(this.color);	
		cell.setEditable(false);
		return cell;
	}

}

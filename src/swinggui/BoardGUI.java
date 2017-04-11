package swinggui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;

public class BoardGUI extends JPanel {
	
	private int height; 
	private int width;
	private int cellWidth = 20;

	public BoardGUI(Board b) {
		this.height = b.getNumRows();
		this.width = b.getNumColumns();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "Board"));
		
		add(MakeGridLayout());
	}
	
	private Component MakeGridLayout() {
		JPanel grid = new JPanel();
		
		grid.setLayout(new GridLayout(this.height, this.width));
		
		
		
		return grid;
	}

}

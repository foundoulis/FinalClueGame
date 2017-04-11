package swinggui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardGUI extends JPanel {
	
	private int height; 
	private int width;
	private int cellWidth = 20;
	
	private List<List<BoxGUI>> boxes;

	public BoardGUI(Board b) {
		this.height = b.getNumRows();
		this.width = b.getNumColumns();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "Board"));
		
		convertBoard(b);
		
		add(drawBoard());
	}
	
	//For drawing the players.
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
	}

	//For drawing the boxes. 
	private Component drawBoard() {
		JPanel panelGrid = new JPanel();
		panelGrid.setLayout(new GridLayout(this.height,0));
		
		for (List<BoxGUI> list: boxes) {
			for (BoxGUI box: list) {
				panelGrid.add(box.toComponent());
			}
		}
		
		return panelGrid;
	}
	
	private class GridListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			//get the click location
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}
		
	}

	public void convertBoard(Board b) {
		boxes = new ArrayList<List<BoxGUI>>();
		for (int i = 0; i < b.getNumRows(); i++) {
			List<BoxGUI> tmp = new ArrayList<BoxGUI>();
			for (int j = 0; j < b.getNumColumns(); j++) {
				tmp.add(new BoxGUI(b.getCellAt(i, j))); // j and i are backwards because rader.
			}
			boxes.add(tmp);
		}
	}
	
}

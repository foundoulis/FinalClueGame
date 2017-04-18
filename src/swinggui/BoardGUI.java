package swinggui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import player.Player;
import clueGame.Board;
import clueGame.BoardCell;

public class BoardGUI extends JPanel {
	public final static int CELL_SIZE = 24;
	public final int CELL_MARGIN = 2;
	private Board board;
	
	private List<List<BoxGUI>> boxes;
	public boolean paintTargets = false;
	
	private Point lastClickLocation;
	
	public BoardGUI(Board b) {
		this.board = b;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		convertBoard(b);
		addMouseListener(new GridListener(this));
	}
	
	@Override
	public void paintComponent(Graphics g) {
	   super.paintComponent(g);

		// Draw cells
		for (List<BoxGUI> list: boxes) {
			for (BoxGUI box: list) {
				box.draw(g, CELL_SIZE, CELL_MARGIN);
			}
		}
		
		// Draw targets
		if (paintTargets) {
			for (BoardCell target : board.getTargets()) {
				System.out.println("paint target");
				BoxGUI box = new BoxGUI(target);
				box.draw(g, CELL_SIZE, CELL_MARGIN, true);
			}
		}
		
		// Draw players
		for(Player player : board.getPlayers()) {
			(new PlayerDot(player)).draw(g, CELL_SIZE, CELL_MARGIN);
		}
	}

	//For drawing the boxes. 
	private Component drawBoard() {
		JPanel panelGrid = new JPanel();
		panelGrid.setLayout(new GridLayout(CELL_SIZE * board.getNumRows(), 0));
		
		// Draw cells
		for (List<BoxGUI> list: boxes) {
			for (BoxGUI box: list) {
				panelGrid.add(box.toComponent());
			}
		}
		
		return panelGrid;
	}
	
	private class GridListener implements MouseListener {
		private BoardGUI b;
		
		public GridListener(BoardGUI b) {
			this.b = b;
		}

		@Override
		public void mouseClicked(MouseEvent event) {
			System.out.println("click");
			Point point = event.getPoint();
			
			System.out.println("p " + point.x/CELL_SIZE + " " + point.y/CELL_SIZE);
			BoardCell target = board.getCellAt(point.y/CELL_SIZE, point.x/CELL_SIZE);
			Set<BoardCell> targets = board.getTargets();
			if (targets.contains(target)) {
				// Row and column get switched so much that I can't keep track anymore.
				// For some dumb reason this works.
				target = board.getCellAt(target.getRow(), target.getColumn());
				board.completeHumanTurn(target);
			} else {
				JOptionPane.showMessageDialog(b, "That is not a target.");
			}
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
	
	public Point getLastClickLocation() {
		return lastClickLocation;
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

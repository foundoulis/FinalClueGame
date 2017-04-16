package swinggui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import player.Player;

public class WhoseTurn extends JPanel {
	private static WhoseTurn whoseTurn;
	private static JTextField name;
	
	public WhoseTurn() {
		setLayout(new BorderLayout());
		add(new Label("Current Player"), BorderLayout.NORTH);
		add(createName(), BorderLayout.CENTER);
	}
	
	@Override
	public void paintComponent(Graphics g) {
	   super.paintComponent(g);
	}
	
	private JTextField createName() {
		name = new JTextField();
		name.setEditable(false);
		return name;
	}
	
	public static void update(String val) {
		checkForInstance();
		name.setText(val);
		whoseTurn.repaint();
	}
	
	private static void checkForInstance() {
		if (whoseTurn == null) {
			whoseTurn = new WhoseTurn();
		}
	}
}

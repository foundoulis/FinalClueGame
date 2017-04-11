package swinggui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import card.Card;

public class DetectiveNotesGUI extends JDialog {

	public DetectiveNotesGUI() {
		Board board = Board.getInstance();
		
		setLayout(new GridLayout(3, 2));
		setSize(400, 600);
		add(createCheckListPanel("People", board.getPeopleCards()));
		add(createPanel("Person Guess"));
		add(createCheckListPanel("Rooms", board.getRoomCards()));
		add(createPanel("Room Guess"));
		add(createCheckListPanel("Weapons", board.getWeaponCards()));
		add(createPanel("Weapon Guess"));
	}
	
	private JPanel createCheckListPanel(String title, List<Card> items) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), title));
		return panel;
	}
	
	private JPanel createPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), title));
		return panel;
	}

}

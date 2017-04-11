package swinggui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Card;
import clueGame.Board;

@SuppressWarnings("serial")
public class DetectiveNotesGUI extends JDialog {

	public DetectiveNotesGUI() {
		Board board = Board.getInstance();
		
		setLayout(new GridLayout(3, 2));
		setSize(500, 600);
		add(createCheckListPanel("People", board.getPeopleCards()));
		add(createDropdownPanel("Person Guess", board.getPeopleCards()));
		add(createCheckListPanel("Rooms", board.getRoomCards()));
		add(createDropdownPanel("Room Guess", board.getRoomCards()));
		add(createCheckListPanel("Weapons", board.getWeaponCards()));
		add(createDropdownPanel("Weapon Guess", board.getWeaponCards()));
	}
	
	private JPanel createCheckListPanel(String title, List<Card> cards) {
		JPanel panel = createPanel(title);
		panel.setLayout(new GridLayout(0, 2));
		
		for (Card c : cards) {
			JCheckBox chkBox = new JCheckBox();
			chkBox.setText(c.getName());
			panel.add(chkBox);
		}
		return panel;
	}
	
	private JPanel createDropdownPanel(String title, List<Card> cards) {
		JPanel panel = createPanel(title);

		ArrayList<String> cardNames = new ArrayList<String>();
		cardNames.add("Unsure"); // Each combobox needs this option no matter what
		for (Card c : cards) {
			cardNames.add(c.getName());
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox cbox = new JComboBox(cardNames.toArray());
		panel.add(cbox);
		return panel;
	}
	
	private JPanel createPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), title));
		return panel;
	}

}

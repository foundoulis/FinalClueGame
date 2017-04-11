package swinggui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import player.HumanPlayer;
import clueGame.Board;

@SuppressWarnings("serial")
public class MyCardsGUI extends JPanel {

	public MyCardsGUI(HumanPlayer humanPlayer) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));

		Board board = Board.getInstance();
		add(new CardPanel("Weapons", board.getWeaponCards()));
		add(new CardPanel("People", board.getPeopleCards()));
		add(new CardPanel("Rooms", board.getRoomCards()));
	}
}

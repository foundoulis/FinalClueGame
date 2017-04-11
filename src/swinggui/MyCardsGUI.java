package swinggui;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import player.HumanPlayer;
import card.Card;
import clueGame.Board;

@SuppressWarnings("serial")
public class MyCardsGUI extends JPanel {

	public MyCardsGUI(HumanPlayer humanPlayer) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		ArrayList<Card> weaponCards = new ArrayList<Card>();
		ArrayList<Card> personCards = new ArrayList<Card>();
		ArrayList<Card> roomCards = new ArrayList<Card>();
		for (Card c : humanPlayer.getCards()) {
			switch (c.getType()) {
			case WEAPON:
				weaponCards.add(c);
				break;
			case PERSON:
				personCards.add(c);
				break;
			case ROOM:
				roomCards.add(c);
				break;
			}
		}
		add(new CardPanel("Weapons", weaponCards));
		add(new CardPanel("People", personCards));
		add(new CardPanel("Rooms", roomCards));
	}
}

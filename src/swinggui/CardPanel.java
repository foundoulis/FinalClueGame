package swinggui;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Card;

@SuppressWarnings("serial")
public class CardPanel extends JPanel {

	public CardPanel(String title, List<Card> cards) {
		setBorder(new TitledBorder(new EtchedBorder(), title));
		for (Card c : cards) {
			JTextField cardNameFld = new JTextField(20);
			cardNameFld.setEditable(false);
			cardNameFld.setText(c.getName());
			add(cardNameFld);
		}
	}
}

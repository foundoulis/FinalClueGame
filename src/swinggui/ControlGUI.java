package swinggui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Card;
import clueGame.Board;
import player.HumanPlayer;

@SuppressWarnings("serial")
public class ControlGUI extends JPanel {
	private static Board board;
	private static HumanPlayer humanPlayer;

	public ControlGUI() {
		setLayout(new GridLayout(3, 1));
		add(createMyCards());
		add(createTurnIndicator());
		add(createNextPlayer());
		add(createMakeAccusation());
		add(createDiceRoll());
		add(createCurrentGuess());
		add(createGuessResult());
	}

	private Component createTurnIndicator() {
		JPanel turn = new JPanel();
		turn.setLayout(new BorderLayout());
		turn.add(createLabel("Current Player"), BorderLayout.NORTH);
		JTextField text = new JTextField();
		text.setEditable(false);
		turn.add(text, BorderLayout.CENTER);
		return turn;
	}

	private Component createLabel(String text) {
		JLabel label = new JLabel();
		label.setText(text);
		return label;
	}

	private Component createGuessResult() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());
		result.add(createLabel("Guess Result"), BorderLayout.NORTH);
		result.add(createLabel("Response: "), BorderLayout.WEST);
		JTextField text = new JTextField();
		text.setEditable(false);
		result.add(text, BorderLayout.CENTER);
		return result;
	}

	private Component createCurrentGuess() {
		JPanel guess = new JPanel();
		guess.setLayout(new BorderLayout());
		guess.add(createLabel("Guess"), BorderLayout.NORTH);
		guess.add(createLabel("Guess: "), BorderLayout.WEST);
		JTextField text = new JTextField();
		guess.add(text, BorderLayout.CENTER);
		return guess;
	}

	private Component createDiceRoll() {
		JPanel dice = new JPanel();
		dice.setLayout(new BorderLayout());
		dice.add(createLabel("Die"), BorderLayout.NORTH);
		dice.add(createLabel("Roll: "), BorderLayout.WEST);
		JTextField text = new JTextField();
		text.setEditable(false);
		dice.add(text, BorderLayout.CENTER);
		return dice;
	}

	private Component createMakeAccusation() {
		JButton accuse = new JButton();
		accuse.setText("Make an accusation");
		return accuse;
	}

	private Component createNextPlayer() {
		JButton next = new JButton();
		next.setText("Next Player");
		return next;
	}
	
	private Component createMyCards() {
		JPanel myCardsPanel = new JPanel();
		myCardsPanel.setLayout(new BoxLayout(myCardsPanel, BoxLayout.Y_AXIS));
		myCardsPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));

		// TODO: have this happen on the board class instead of combining them in the first place
		// Split cards into lists by type.
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
		myCardsPanel.add(new CardPanel("Weapons", weaponCards));
		myCardsPanel.add(new CardPanel("People", personCards));
		myCardsPanel.add(new CardPanel("Rooms", roomCards));
		
		return myCardsPanel;
	}

	public static void main(String[] args) {
		// Set up board
		board = Board.getInstance();
		board.setConfigFiles(
				"ICJK_ClueLayout.csv",
				"ICJK_Legend.txt",
				"TDNFTP_players.txt",
				"TDDF_weapons.txt",
				"TDDF_people.txt");
		board.initialize();
		humanPlayer = board.getHumanPlayer();
		
		// Set up GUI
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Clue");
		frame.setSize(600, 200);
		
		ControlGUI gui = new ControlGUI();
		frame.add(gui, BorderLayout.CENTER);

		JOptionPane.showMessageDialog(frame, "You are " + humanPlayer.getName() + ", press Next Player to begin play");
		
		frame.setVisible(true);
	}

}

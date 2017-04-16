package swinggui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clueGame.Board;
import clueGame.ClueGame;

@SuppressWarnings("serial")
public class ControlGUI extends JPanel {
	
	private WhoseTurn whoseTurn;
	private JPanel turnIndicatorPanel;


	public ControlGUI() {
		whoseTurn = new WhoseTurn();
		
		setLayout(new GridLayout(3, 1));
		add(whoseTurn);
		add(createNextPlayer());
		add(createMakeAccusation());
		add(createDiceRoll());
		add(createCurrentGuess());
		add(createGuessResult());
	}

	private Component createGuessResult() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());
		result.add(new Label("Guess Result"), BorderLayout.NORTH);
		result.add(new Label("Response: "), BorderLayout.WEST);
		JTextField text = new JTextField();
		text.setEditable(false);
		result.add(text, BorderLayout.CENTER);
		return result;
	}

	private Component createCurrentGuess() {
		JPanel guess = new JPanel();
		guess.setLayout(new BorderLayout());
		guess.add(new Label("Guess"), BorderLayout.NORTH);
		guess.add(new Label("Guess: "), BorderLayout.WEST);
		JTextField text = new JTextField();
		guess.add(text, BorderLayout.CENTER);
		return guess;
	}

	private Component createDiceRoll() {
		JPanel dice = new JPanel();
		dice.setLayout(new BorderLayout());
		dice.add(new Label("Die"), BorderLayout.NORTH);
		dice.add(new Label("Roll: "), BorderLayout.WEST);
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
		next.addActionListener(e -> Board.getInstance().handleNextPlayerClickEvent());
		return next;
	}
	
	public void updateWhoseTurn(String playerName) {
		this.whoseTurn.update(playerName);
	}
}

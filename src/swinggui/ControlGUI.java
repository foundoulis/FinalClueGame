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
	
	private JTextField whoseTurnText;
	private JPanel turnIndicatorPanel;


	public ControlGUI() {		
		setLayout(new GridLayout(3, 1));
		add(createTurnIndicator());
		add(createNextPlayer());
		add(createMakeAccusation());
		add(createDiceRoll());
		add(createCurrentGuess());
		add(createGuessResult());
	}

	private Component createTurnIndicator() {
		turnIndicatorPanel = new JPanel();
		turnIndicatorPanel.setLayout(new BorderLayout());
		turnIndicatorPanel.add(createLabel("Current Player"), BorderLayout.NORTH);
		whoseTurnText = new JTextField();
		whoseTurnText.setEditable(false);
		turnIndicatorPanel.add(whoseTurnText, BorderLayout.CENTER);
		return turnIndicatorPanel;
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
		next.addActionListener(e -> Board.getInstance().handleNextPlayerClickEvent());
		return next;
	}
	
	public void setWhoseTurnText(String playerName) {
/*		System.out.println("playerName " + playerName);
		this.whoseTurnText = new JTextField(playerName);*/
		this.whoseTurnText.setText(playerName);
		System.out.println("the text " +this.whoseTurnText.getText());
		turnIndicatorPanel.validate();
		turnIndicatorPanel.revalidate();
		turnIndicatorPanel.repaint();
		this.revalidate();
		this.repaint();
/*		this.whoseTurnText.setVisible(true);
		this.whoseTurnText.validate();
		this.repaint();*/
	}
}

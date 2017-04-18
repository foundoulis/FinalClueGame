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
	
	private static ControlGUI theInstance;
	
	private WhoseTurn whoseTurn;
	private JPanel turnIndicatorPanel;

	public JPanel diceTextPanel;
	public JTextField diceTextField;
	
	public JTextField playerTextField;
	
	public static ControlGUI getInstance() {
		if (theInstance == null) {
			theInstance = new ControlGUI();
		}
		return theInstance;
	}
	
	
	private ControlGUI() {
		//whoseTurn = new WhoseTurn();
		
		setLayout(new GridLayout(3, 1));
		add(createTurnDisplay());
		add(createNextPlayer());
		add(createMakeAccusation());
		add(createDiceRoll());
		add(createCurrentGuess());
		add(createGuessResult());
	}

	private Component createTurnDisplay() {
		JPanel playerNamePanel = new JPanel();
		playerNamePanel.setLayout(new BorderLayout());
		playerNamePanel.add(new Label("Current Player"), BorderLayout.NORTH);
		playerNamePanel.add(new Label("Name: "), BorderLayout.WEST);
		JTextField text = new JTextField("Your turn.");
		text.setEditable(false);
		playerNamePanel.add(text, BorderLayout.CENTER);
		
		this.playerTextField = text;
		
		return playerNamePanel;
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
		
		//Local variables to update on command
		this.diceTextPanel = dice;
		this.diceTextField = text;
		
		return dice;
	}
	
	public void UpdateDiceRoll(int n) {
		String roll = "" + n + "";
		this.diceTextField.setText(roll);
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
	
/*	public void updateWhoseTurn(String playerName) {
		this.whoseTurn.update(playerName);
	}*/


	public void UpdatePlayerTurn(String s) {
		this.playerTextField.setText(s);
	}
	
	
}

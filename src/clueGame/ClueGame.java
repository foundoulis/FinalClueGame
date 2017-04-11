package clueGame;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import player.HumanPlayer;
import swinggui.ControlGUI;
import swinggui.MyCardsGUI;

@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	private static Board board;
	private static HumanPlayer humanPlayer;


	public ClueGame() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue");
		setSize(600, 200);

		add(new ControlGUI(), BorderLayout.SOUTH);
		add(new MyCardsGUI(humanPlayer), BorderLayout.EAST);
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

		// Display game
		ClueGame game = new ClueGame();
		game.setVisible(true);
		JOptionPane.showMessageDialog(game, "You are " + humanPlayer.getName() + ", press Next Player to begin play");
	}

}

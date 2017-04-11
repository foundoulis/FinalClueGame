package clueGame;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import player.HumanPlayer;
import swinggui.BoardGUI;
import swinggui.ControlGUI;
import swinggui.DetectiveNotesGUI;
import swinggui.MyCardsGUI;

@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	private static Board board;
	private static HumanPlayer humanPlayer;
	private DetectiveNotesGUI detectiveNotes;

	public ClueGame() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue");
		setSize(1200, 800);

		// Create menu
		detectiveNotes = new DetectiveNotesGUI();
		setJMenuBar(createMenu());
		
		// Create GUI
		add(new ControlGUI(), BorderLayout.SOUTH);
		add(new MyCardsGUI(humanPlayer), BorderLayout.EAST);
		add(new BoardGUI(board), BorderLayout.CENTER);
	}
	
	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		
		JMenuItem fileItem = new JMenuItem("Detective Notes");
		fileItem.addActionListener(e -> detectiveNotes.setVisible(true));
		fileMenu.add(fileItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);	
		return menuBar;
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

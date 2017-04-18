package clueGame;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private static ClueGame cg;
	private BoardGUI boardGui;
	private ControlGUI controlGui;

	public static ClueGame getInstance() {
		if (cg == null)
			cg = new ClueGame();
		return cg;
	}
	
	private ClueGame() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(1200, 800);

		// Create menu
		detectiveNotes = new DetectiveNotesGUI();
		setJMenuBar(createMenu());
		
		// Create GUI
		this.controlGui = ControlGUI.getInstance();
		this.boardGui = new BoardGUI(board);
		
		add(this.controlGui, BorderLayout.SOUTH);
		add(new MyCardsGUI(humanPlayer), BorderLayout.EAST);
		add(this.boardGui, BorderLayout.CENTER);
	}
	
	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		
		JMenuItem fileItem = new JMenuItem("Detective Notes");
		fileItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				detectiveNotes.setVisible(true);
			}
			
		});
		fileMenu.add(fileItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
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
		ClueGame game = ClueGame.getInstance();
		game.setVisible(true);
		JOptionPane.showMessageDialog(game, "You are " + humanPlayer.getName() + ", press Next Player to begin play");
	}

	public void setPaintTargets(boolean b) {
		boardGui.paintTargets = b;
	}

}

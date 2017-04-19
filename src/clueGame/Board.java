package clueGame;

import java.awt.Color;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import swinggui.AccuseWindow;
import swinggui.BoardGUI;
import swinggui.ControlGUI;
import swinggui.WhoseTurn;
import card.Card;
import card.CardType;
import exceptions.BadConfigFormatException;

@SuppressWarnings("serial")
public class Board extends JPanel {

	// variable used for singleton pattern
	private static Board theInstance;
	// ctor is private to ensure only one can be created
	private String boardConfigFile;
	private String legenConfigFile;
	private String playersConfigFile;
	private String weaponConfigFile;
	private String peopleConfigFile;
	private final int MAX_BOARD_SIZE = 50;
	private BoardCell[][] grid;
	private int numRows, numCols;
	private Map<Character, String> legendMap;
	private Map<BoardCell, Set<BoardCell>> adjMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private List<Player> players;
	private HumanPlayer humanPlayer;
	private int currentPlayerIndex = -1;
	private boolean waitingForHumanToSelectTarget = false;
	private boolean canMakeAccusation = true;
	private List<Card> deck;
	private List<Card> peopleCards;
	private List<Card> roomCards;
	private List<Card> weaponCards;
	private Solution answer;
	
	private Random rand;
	private int diceRoll;
	private boolean imSoDoneWithThisProject = false;

	// this method returns the only Board
	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}

	public void setConfigFiles(String layoutLoc, String legendLoc, String playerLoc, String weaponConfigFile, String peopleConfigFile) {
		this.boardConfigFile = layoutLoc;
		this.legenConfigFile = legendLoc;
		this.playersConfigFile = playerLoc;
		this.weaponConfigFile = weaponConfigFile;
		this.peopleConfigFile = peopleConfigFile;
	}
	
	public void setConfigFiles(String layoutLoc, String legendLoc, String playerLoc) {
		this.setConfigFiles(layoutLoc, legendLoc, playerLoc, "TDDF_weapons.txt", "TDDF_people.txt");
		/*
		 * The above function is set so that the tests that predate the addition of weapons and people are loaded, these are default values
		 * This is not problematic as when the real function is called there is not an absence of those files. Ie, they must be present 
		 * for the game to even work.
		 */
	}

	public void initialize() {
		reset();
		rand.setSeed(System.currentTimeMillis());

		try {
			loadRoomConfig();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		try {
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		try {
			loadPlayerConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		loadDeck();
		dealDeck(new Random());
		calcAdjacencies();
	}

	public void reset() {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		adjMap = new HashMap<BoardCell, Set<BoardCell>>();
		deck = new ArrayList<Card>();
		peopleCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();
		weaponCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		rand = new Random();
	}

	public void dealDeck(Random rand) {
		final int cardsPerPlayer = (deck.size() - 3) / players.size();
		Card randomCard;
		
		// Get weapon card
		do {
			randomCard = deck.get(rand.nextInt(deck.size()));
		} while (randomCard.hasBeenDealt() || randomCard.getType() != CardType.WEAPON);
		Card weapon = randomCard;
		randomCard.isSolution();

		// Get room card
		do {
			randomCard = deck.get(rand.nextInt(deck.size()));
		} while (randomCard.hasBeenDealt() || randomCard.getType() != CardType.ROOM);
		Card room = randomCard;
		randomCard.isSolution();

		// Get person card
		do {
			randomCard = deck.get(rand.nextInt(deck.size()));
		} while (randomCard.hasBeenDealt() || randomCard.getType() != CardType.PERSON);
		Card person = randomCard;
		randomCard.isSolution();

		answer = new Solution(person, room, weapon);

		for (Player player : players) {
			for (int i = 0; i < cardsPerPlayer; i++) {
				do {
					randomCard = deck.get(rand.nextInt(deck.size()));
				} while (randomCard.hasBeenDealt());
				randomCard.dealToPlayer(player);
			}
		}
	}

	public void loadDeck() {
		loadWeaponConfig();
		loadPeopleConfig();
		
		// Create room cards
		for (String room : legendMap.values()) {
			// TODO: this could be done better considering that the legend file
			// has a way of knowing if a room is a card or not.
			if (!room.equals("Walkway") && !room.equals("Closet")) {
				Card card = new Card(CardType.ROOM, room);
				deck.add(card);
				roomCards.add(card);
			}
		}
	}

	public void calcAdjacencies() {
		for (int x = 0; x < numRows; x++) {
			for (int y = 0; y < numCols; y++) {
				BoardCell boardKey;
				BoardCell boardValue;
				Set<BoardCell> adjacent = new HashSet<BoardCell>();
				boardKey = grid[y][x];
				if (boardKey != null) {
					if (boardKey.isDoorway()) {
						switch (boardKey.getDoorDirection()) {
						case RIGHT:
							boardValue = grid[y + 1][x];
							if (boardValue.isWalkway()) {
								adjacent.add(boardValue);
							}
							break;
						case LEFT:
							boardValue = grid[y - 1][x];
							if (boardValue.isWalkway()) {
								adjacent.add(boardValue);
							}
							break;
						case UP:
							boardValue = grid[y][x - 1];
							if (boardValue.isWalkway()) {
								adjacent.add(boardValue);
							}
							break;
						case DOWN:
							boardValue = grid[y][x + 1];
							if (boardValue.isWalkway()) {
								adjacent.add(boardValue);
							}
							break;
						case NONE:
							break;
						}
					} else if (!boardKey.isRoom()) {
						if (x - 1 >= 0) {
							boardValue = grid[y][x - 1];
							if (boardValue.isWalkway() || boardValue.getDoorDirection() == DoorDirection.DOWN) {
								adjacent.add(boardValue);
							}
						}
						if (y - 1 >= 0) {
							boardValue = grid[y - 1][x];
							if (boardValue.isWalkway() || boardValue.getDoorDirection() == DoorDirection.RIGHT) {
								adjacent.add(boardValue);
							}

						}
						if (x + 1 < numRows) {
							boardValue = grid[y][x + 1];
							if (boardValue.isWalkway() || boardValue.getDoorDirection() == DoorDirection.UP) {
								adjacent.add(boardValue);
							}

						}
						if (y + 1 < numCols) {
							boardValue = grid[y + 1][x];
							if (boardValue.isWalkway() || boardValue.getDoorDirection() == DoorDirection.LEFT) {
								adjacent.add(boardValue);
							}
						}
					}
				}
				adjMap.put(boardKey, adjacent);
			}
		}
	}

	public void loadRoomConfig() throws BadConfigFormatException {
		// Create Legend
		FileReader reader = null;
		try {
			reader = new FileReader(legenConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(reader);
		legendMap = new HashMap<Character, String>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			int tracker = 3;
			while (line.charAt(tracker) != ',')
				tracker++;
			legendMap.put(line.charAt(0), line.substring(3, tracker));
			if (!line.substring(tracker + 2).equals("Card") && !line.substring(tracker + 2).equals("Other")) {
				in.close();
				throw new BadConfigFormatException();
			}
		}
		in.close();
	}

	public void loadBoardConfig() throws BadConfigFormatException {
		int numColsPast = -1;
		FileReader reader = null;
		try {
			reader = new FileReader(boardConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grid = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		Scanner in = new Scanner(reader);
		numRows = 0;
		String line = "";
		while (in.hasNextLine()) {
			line = in.nextLine();
			numCols = 0;
			char last = ',';
			for (int i = 0; i < line.length(); i++) {
				if (last == ',') {
					if (!legendMap.containsKey(line.charAt(i))) {
						in.close();
						throw new BadConfigFormatException();
					}
					grid[numCols][numRows] = new BoardCell(numCols, numRows, line.charAt(i));
					numCols++;
				} else if (line.charAt(i) != ',') {
					if (grid[numCols - 1][numRows] != null) {
						switch (line.charAt(i)) {
						case 'D':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.DOWN);
							break;
						case 'U':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.UP);
							break;
						case 'R':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.RIGHT);
							break;
						case 'L':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.LEFT);
							break;
						default:
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.NONE);
							break;
						}
					}

				}
				last = line.charAt(i);
			}
			if (numColsPast != -1 && numColsPast != numCols) {
				in.close();
				throw new BadConfigFormatException();
			}
			numColsPast = numCols;
			numRows++;

		}
		in.close();
	}

	public void calcTargets(int row, int col, int pathLen) {
		targets.clear();
		visited.clear();
		calcTargetsRecursion(grid[col][row], pathLen);
	}

	private void calcTargetsRecursion(BoardCell currentCell, int pathLength) {
		Set<BoardCell> adjCells = adjMap.get(currentCell);
		for (BoardCell option : adjCells) {
			// Check if the option was a past position
			boolean didVisit = false;
			if (!visited.isEmpty()) {
				for (BoardCell past : visited) {
					if (past.equals(option))
						didVisit = true;
				}
			}
			// Do something with object if it was not a past position
			if (!didVisit) {
				if (pathLength < 2 || option.isDoorway()) {
					targets.add(option); // add if it number of steps has been met.
				} else {
					// keep going along path if still have more steps
					visited.add(currentCell);
					calcTargetsRecursion(option, pathLength - 1);
					visited.remove(currentCell);
				}
			}

		}

	}

	public void loadPlayerConfig() throws FileNotFoundException {
		FileReader reader = new FileReader(playersConfigFile);
		Scanner in = new Scanner(reader);
		while (in.hasNextLine()) {
			String[] split = in.nextLine().split(",");
			
			// Get color
			Color color = null;
			try {
			    Field field = Class.forName("java.awt.Color").getField(split[2].toLowerCase());
			    color = (Color)field.get(null);
			} catch (Exception e) {
			    color = Color.BLACK; // Not defined
			}

			int row = Integer.parseInt(split[4]);
			int col = Integer.parseInt(split[3]);
			if (split[0].equals("Human")) {
				humanPlayer = new HumanPlayer(split[1], color, row, col);
				players.add(humanPlayer);
			} else {
				players.add(new ComputerPlayer(split[1], color, row, col));
			}
		}
		in.close();
	}

	public void loadWeaponConfig() {
		try {
			List<String> weaponsList = Files.readAllLines(Paths.get(this.weaponConfigFile));
			for (String s : weaponsList) {
				Card card = new Card(CardType.WEAPON, s);
				deck.add(card);
				weaponCards.add(card);
			}
		} catch (IOException e) {
			System.out.println("There is no weapon config file by that name");
		}
	}

	public void loadPeopleConfig() {
		try {
			List<String> peopleList = Files.readAllLines(Paths.get(this.peopleConfigFile));
			for (String s : peopleList) {
				Card card = new Card(CardType.PERSON, s);
				deck.add(card);
				peopleCards.add(card);
			}
		} catch (IOException e) {
			System.out.println("There is no people config file by that name.");
		}
	}

	public boolean checkAccusation(Solution accusation) {
		return answer.equals(accusation);
	}

	public Card handleSuggestion(Solution suggestion, Player accuser) {
		int pos = players.indexOf(accuser) + 1;
		if (pos >= players.size()) {
			pos = 0;
		}
		while (pos != players.indexOf(accuser)) {
			Card c = players.get(pos).disproveSuggestion(suggestion);
			if (c != null) {
				return c;
			}

			pos++;
			if (pos >= players.size()) {
				pos = 0;
			}
		}
		return null;
	}
	
	private void paintComponent() {
		// TODO draw stuff here
	}
	
	
	////////////////////////////////////////////
	// GETTERS AND SETTERS
	////////////////////////////////////////////

	public Map<Character, String> getLegend() {
		return legendMap;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public BoardCell getCellAt(int row, int col) {
		return grid[col][row];
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return adjMap.get(grid[col][row]);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public Solution getAnswer() {
		return answer;
	}

	/**
	 * For testing purposes.
	 * 
	 * @param soln
	 */
	public void setAnswer(Solution soln) {
		answer = soln;
	}
	
	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}
	
	public List<Card> getPeopleCards() {
		return peopleCards;
	}
	
	public List<Card> getRoomCards() {
		return roomCards;
	}
	
	public List<Card> getWeaponCards() {
		return weaponCards;
	}

	public BoardCell[][] getBoardCells() {
		return this.grid;
	}

	public Player getCurrentPlayer() {
		return this.players.get(currentPlayerIndex);
	}
	
	/////////
	// EVENTS
	/////////

	public void handleNextPlayerClickEvent() {
		if (waitingForHumanToSelectTarget) {
			JOptionPane.showMessageDialog(this, "You must finish your turn before you can go to the next player");
			return;
		} else {
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size(); // Go to next player
		}
		
		//this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();

		Player currentPlayer = getCurrentPlayer();
		
		// TODO this doesn't update the GUI
		//System.out.println(currentPlayer.getName());
		WhoseTurn.update(currentPlayer.getName());
		ControlGUI.getInstance().UpdatePlayerTurn(currentPlayer.getName());
		
		this.diceRoll = this.rand.nextInt(4) + 1;
		ControlGUI.getInstance().UpdateDiceRoll(this.diceRoll);
		
		this.calcTargets(
				currentPlayer.getRow(),
				currentPlayer.getColumn(),
				this.diceRoll);
		
		ClueGame cg = ClueGame.getInstance();
		boolean isHuman = currentPlayer instanceof HumanPlayer;
		if (isHuman) {
			canMakeAccusation = true;
			waitingForHumanToSelectTarget = true;
			cg.setPaintTargets(true);
		} else {
			if (((ComputerPlayer) currentPlayer).isMakeAccusationOnNextTurn()) {
				boolean isRight = checkAccusation(((ComputerPlayer) currentPlayer).getAccusation());
				if (isRight) {
					JOptionPane.showMessageDialog(this, "You lost! The computer won!");
					System.exit(0);
				} else {
					return;
				}
			}
			
			cg.setPaintTargets(false);
			BoardCell target = ((ComputerPlayer) currentPlayer).pickLocation(targets);
			// Row and column get switched so much that I can't keep track anymore.
			// For some dumb reason this works.
			BoardCell drawTarget = getCellAt(target.getRow(), target.getColumn());
			currentPlayer.moveToTarget(drawTarget);
			
			if (target.isRoom()) {
				Solution suggestion = ((ComputerPlayer) currentPlayer).createSuggestion();
				Card card = handleSuggestion(suggestion, currentPlayer);
				if (card != null) {
					currentPlayer.addSeenCard(card);
				} else {
					((ComputerPlayer) currentPlayer).setAccusation(suggestion);
					JOptionPane.showMessageDialog(this, "Computer player will make accusation on next turn.");
				}
			}
		}
		
		cg.repaint();
	}
	
	public void completeHumanTurn() {
		Player currentPlayer = getCurrentPlayer();
		BoardCell target = getCellAt(currentPlayer.getRow(), currentPlayer.getColumn());
		// Row and column get switched so much that I can't keep track anymore.
		// For some dumb reason this works.
		target = getCellAt(target.getRow(), target.getColumn());
		completeHumanTurn(target);
	}

	public void completeHumanTurn(BoardCell target) {
		BoardCell actualTarget = getCellAt(target.getRow(), target.getColumn());
		if (actualTarget.isRoom()) {
			imSoDoneWithThisProject = true;
			String roomName = legendMap.get(actualTarget.getInitial());
			new AccuseWindow(this.peopleCards, this.weaponCards, roomName);
		}
		
		ClueGame cg = ClueGame.getInstance();
		waitingForHumanToSelectTarget = false;
		getCurrentPlayer().moveToTarget(target);
		cg.setPaintTargets(false);
		cg.repaint();
	}

	public void handleAccusationClickEvent() {
		if (this.currentPlayerIndex != 0) {
			JOptionPane.showMessageDialog(this, "You may only accuse on your turn.");
		} else if (!canMakeAccusation) {
			JOptionPane.showMessageDialog(this, "You may only accuse once during your turn.");
		} else { // the accusation window
			String roomName = getCurrentPlayerRoomName();
			if (roomName.equalsIgnoreCase("walkway")) {
				JOptionPane.showMessageDialog(this, "You must be in a room to accuse.");
			} else {
				new AccuseWindow(this.peopleCards, this.weaponCards, roomName);
			}
		}
	}

	public void setPlayerAccusation(String [] guesses) {
		//Run the guess processing, person is 0, weapon is 1, room is whereever they are
		if (imSoDoneWithThisProject) {
			imSoDoneWithThisProject = false;
			Player currentPlayer = getCurrentPlayer();
			
			Solution guess = new Solution(guesses[0], getCurrentPlayerRoomName(), guesses[1]);
			Card card = handleSuggestion(guess, currentPlayer);
			
			if (card != null) {
				currentPlayer.addSeenCard(card);
			} else {
				JOptionPane.showMessageDialog(this, "No new clue!");
				for (Player p : players) {
					if (p.getName().equalsIgnoreCase(guesses[0])) {
						BoardCell drawTarget = getCellAt(currentPlayer.getRow(), currentPlayer.getColumn());
						p.moveToTarget(drawTarget);
						break;
					}
				}
				completeHumanTurn();
			}
		} else {
			canMakeAccusation = false;
			Solution guess = new Solution(guesses[0], getCurrentPlayerRoomName(), guesses[1]);
			if (checkAccusation(guess)) {
				JOptionPane.showMessageDialog(this, "You won!");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(this, "That was not correct!");
				completeHumanTurn();
			}
		}
	}
	
	private String getCurrentPlayerRoomName() {
		Player currentPlayer = getCurrentPlayer();
		BoardCell room = getCellAt(currentPlayer.getRow(), currentPlayer.getColumn());
		return this.legendMap.get(room.getInitial());
	}
}

package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;

import card.Card;
import card.CardType;
import exceptions.BadConfigFormatException;

public class Board {

	// variable used for singleton pattern
	private static Board theInstance;
	// ctor is private to ensure only one can be created
	private String boardConfigFile; // TODO: rename these 3 vars
	private String legenConfigFile;
	private String playersConfigFile;
	private String weaponConfigFile;
	private String peopleConfigFile;
	private final int MAX_BOAR_SIZE = 50;
	private BoardCell[][] grid;
	private int numRows, numCols;
	private Map<Character, String> legendMap;
	private Map<BoardCell, Set<BoardCell>> adjMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private List<Player> players;
	private List<Card> deck;
	private Solution answer;

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
		this.setConfigFiles(layoutLoc, legendLoc, playerLoc, "", "");
	}

	public void initialize() {
		reset();

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
		players = new ArrayList<Player>();
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
		if (!weaponConfigFile.isEmpty()) loadWeaponConfig();
		if (!peopleConfigFile.isEmpty()) loadPeopleConfig();
		
		// Create room cards
		for (String room : legendMap.values()) {
			// TODO: this could be done better considering that the legend file
			// has a way of knowing if a room is a card or not.
			if (!room.equals("Walkway") && !room.equals("Closet")) {
				deck.add(new Card(CardType.ROOM, room));
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
		grid = new BoardCell[MAX_BOAR_SIZE][MAX_BOAR_SIZE];
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

	public void calcTargets(int row, int col, int pathLen) {
		targets.clear();
		visited.clear();
		BoardCell startCell = grid[col][row];
		calcTargetsRecursion(startCell, pathLen);
	}

	private void calcTargetsRecursion(BoardCell currentCell, int pathLength) {
		for (BoardCell option : adjMap.get(currentCell)) {
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
					targets.add(option); // add if it number of steps has been
					// met.
				} else {
					// keep going along path if still have more steps
					visited.add(currentCell);
					calcTargetsRecursion(option, pathLength - 1);
					visited.remove(currentCell);
				}
			}

		}

	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public void loadPlayerConfig() throws FileNotFoundException {
		FileReader reader = new FileReader(playersConfigFile);
		Scanner in = new Scanner(reader);
		while (in.hasNextLine()) {
			String[] split = in.nextLine().split(",");
			Color color = null;
			switch (split[2]) {
			case "Green":
				color = Color.GREEN;
				break;
			case "Red":
				color = Color.RED;
				break;
			case "Blue":
				color = Color.BLUE;
				break;
			}
			if (split[0].equals("Human")) {
				players.add(new HumanPlayer(split[1], color, Integer.parseInt(split[3]), Integer.parseInt(split[4])));
			} else {
				players.add(
						new ComputerPlayer(split[1], color, Integer.parseInt(split[3]), Integer.parseInt(split[4])));
			}
		}
		in.close();
	}

	public void loadWeaponConfig() {
		try {
			List<String> weaponsList = Files.readAllLines(Paths.get(this.weaponConfigFile));
			for (String s : weaponsList) {
				deck.add(new Card(CardType.WEAPON, s));
			}
		} catch (IOException e) {
			System.out.println("There is no weapon config file by that name");
		}
	}

	public void loadPeopleConfig() {
		try {
			List<String> peopleList = Files.readAllLines(Paths.get(this.peopleConfigFile));
			for (String s : peopleList) {
				deck.add(new Card(CardType.PERSON, s));
			}
		} catch (IOException e) {
			System.out.println("There is no people config file by that name.");
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Card> getDeck() {
		return deck;
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
}

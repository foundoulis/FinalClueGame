package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import player.ComputerPlayer;
import player.Player;

import card.Card;
import card.CardType;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Solution;
import exceptions.BadConfigFormatException;

public class GameActionTests {

	private static Board board;

	@BeforeClass
	public static void initTheBoard() {
		// initialize board
		// Board is singleton, get the only instance and initialize it
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles(
				"ICJK_ClueLayout.csv",
				"ICJK_Legend.txt",
				"TDNFTP_players.txt",
				"TDDF_weapons.txt",
				"TDDF_people.txt");

		// Loads the board with a fake random deck
		board.reset();
		try {
			board.loadRoomConfig();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		try {
			board.loadBoardConfig();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		try {
			board.loadPlayerConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		board.loadDeck();
		// Distributes 1 weapon, room, person, repeatedly until it runs out and
		// then just distributes rooms
		int[] veryRandom = { 0, 6, 15, 1, 7, 16, 2, 8, 17, 3, 9, 18, 4, 10, 19, 5, 11, 20, 12, 13, 14 };
		board.dealDeck(new FakeRandom(veryRandom));
		board.calcAdjacencies();
	}

	// Tests random selection with no doors nearby
	@Test
	public void selectRandomTarget() {

		// Calculates the available spaces for the player to move
		board.calcTargets(16, 9, 2);

		// Initializes a new computerPlayer
		ComputerPlayer player = new ComputerPlayer("Blocky", Color.RED, 16, 9);

		// Tests three "random" locations
		int[] randomPlaces = { 0, 1, 2 };
		FakeRandom rand = new FakeRandom(randomPlaces);
		BoardCell selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(14, 9), selected);

		selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(18, 9), selected);

		selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(15, 10), selected);
	}

	@Test
	public void selectRoomTarget() {
		// Calculates the available spaces for the player to move
		board.calcTargets(15, 18, 2);

		// Initializes a new computerPlayer
		ComputerPlayer player = new ComputerPlayer("Blocky", Color.RED, 15, 18);

		// Checks to make sure it picked a doorway
		BoardCell selected = player.pickLocation(board.getTargets());
		assertTrue(selected.isDoorway());
	}

	// Tests location selection when there is a door that was the last room
	@Test
	public void selectNewRandom() {
		// Calculates the available spaces for the player to move
		board.calcTargets(7, 11, 2);

		// Initializes a new computerPlayer
		ComputerPlayer player = new ComputerPlayer("Blocky", Color.RED, 7, 11);
		player.setLastRoom(board.getCellAt(6, 11));

		// Computer player randomly picks a location
		int[] randomPlaces = { 0, 3, 7 };
		FakeRandom rand = new FakeRandom(randomPlaces);

		// Test a non-door location
		BoardCell selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(7, 9), selected);

		// Tests the location that contains the door
		selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(6, 11), selected);

		// Test another non-door location
		selected = player.pickLocation(board.getTargets(), rand);
		assertEquals(board.getCellAt(7, 13), selected);
	}

	// Tests to make sure accusations are correctly checked
	@Test
	public void testCheckAccusation() {
		// Set possible solutions
		Solution trueSoln = new Solution("Mrs. Kellie", "Music room", "Bat");
		Solution failSoln1 = new Solution("Mr. Ryan", "Music room", "Bat");
		Solution failSoln2 = new Solution("Mrs. Kellie", "Master Bedroom", "Bat");
		Solution failSoln3 = new Solution("Mrs. Kellie", "Music room", "Gun");

		// Test the true solution
		assertTrue(board.checkAccusation(trueSoln));
		// Test a failing solution with a person, room, and weapon, respectively
		assertFalse(board.checkAccusation(failSoln1));
		assertFalse(board.checkAccusation(failSoln2));
		assertFalse(board.checkAccusation(failSoln3));
	}

	@Test
	public void testCreateSuggestion() {
		final List<Player> players = board.getPlayers();
		for (Player player : players) {
			if (player instanceof ComputerPlayer) {
				// Set room to Master bedroom
				player.setLocation(2, 4);
				List<Card> cards = new ArrayList<Card>();

				// Test with one of each card types
				cards.add(new Card(CardType.WEAPON, "Bat"));
				cards.add(new Card(CardType.PERSON, "Mr. Bob"));
				player.setCards(cards);
				Solution suggestion = ((ComputerPlayer) player).createSuggestion();
				assertTrue(suggestion.getRoom().equals("Master bedroom"));
				assertTrue(suggestion.getWeapon().equals("Bat"));
				assertTrue(suggestion.getPerson().equals("Mr. Bob"));

				// Test with multiple card types
				cards.add(new Card(CardType.WEAPON, "Gun"));
				cards.add(new Card(CardType.PERSON, "Mr. Platoon"));
				player.setCards(cards);
				suggestion = ((ComputerPlayer) player).createSuggestion();
				assertTrue(suggestion.getRoom().equals("Master bedroom"));
				assertTrue(suggestion.getWeapon().equals("Bat") || suggestion.getWeapon().equals("Gun"));
				assertTrue(suggestion.getPerson().equals("Mr. Bob") || suggestion.getPerson().equals("Mr. Platoon"));
			}
		}
	}

	// Independent test, does not require the use of board
	@Test
	public void testDisproveSuggestion() {
		// Set up cards for the test
		Card person = new Card(CardType.PERSON, "Bob");
		Card badperson = new Card(CardType.PERSON, "Bill");
		Card room = new Card(CardType.ROOM, "Bedroom");
		Card badroom = new Card(CardType.ROOM, "Bathroom");
		Card weapon = new Card(CardType.WEAPON, "Gun");
		Card badweapon = new Card(CardType.WEAPON, "Knife");

		// Set up a computer player and give them three of the cards
		ComputerPlayer tester = new ComputerPlayer("Joe", Color.RED, 0, 0);
		tester.addCard(person);
		tester.addCard(room);
		tester.addCard(weapon);

		// Test with only one card correct
		assertEquals(weapon, tester.disproveSuggestion(
				new Solution(badperson.getName(), badroom.getName(), weapon.getName()), new Random()));
		assertEquals(person, tester.disproveSuggestion(
				new Solution(person.getName(), badroom.getName(), badweapon.getName()), new Random()));
		assertEquals(room, tester.disproveSuggestion(
				new Solution(badperson.getName(), room.getName(), badweapon.getName()), new Random()));

		// Tests with all three cards correct
		assertEquals(person, tester.disproveSuggestion(new Solution(person.getName(), room.getName(), weapon.getName()),
				new FakeRandom(0)));
		assertEquals(room, tester.disproveSuggestion(new Solution(person.getName(), room.getName(), weapon.getName()),
				new FakeRandom(1)));
		assertEquals(weapon, tester.disproveSuggestion(new Solution(person.getName(), room.getName(), weapon.getName()),
				new FakeRandom(2)));

		// Test with no cards correct
		assertEquals(null, tester.disproveSuggestion(
				new Solution(badperson.getName(), badroom.getName(), badweapon.getName()), new Random()));
	}

	@Test
	public void testHandleSuggestion() {
		List<Player> players = board.getPlayers();
		Player john = players.get(0);
		Player jane = players.get(1);
		Player brad = players.get(2);

		// Only case where no one can disprove is if it is the answer
		assertEquals(null, board.handleSuggestion(board.getAnswer(), john));
		assertEquals(null, board.handleSuggestion(board.getAnswer(), jane));
		assertEquals(null, board.handleSuggestion(board.getAnswer(), brad));

		// Only Brad can disprove the solution
		Solution test = new Solution(brad.getCards().get(5).getName(), board.getAnswer().getRoom(),
				board.getAnswer().getWeapon());

		// He is the accuser, returns null
		assertEquals(null, board.handleSuggestion(test, brad));

		// He is not the accuser, returns the person he has
		assertEquals(brad.getCards().get(5), board.handleSuggestion(test, jane));

		// Only John, a human can disprove the solution
		test = new Solution(john.getCards().get(2).getName(), board.getAnswer().getRoom(),
				board.getAnswer().getWeapon());

		// He is the accuser, returns null
		assertEquals(null, board.handleSuggestion(test, john));

		// He is not the accuser, returns the person he has
		assertEquals(john.getCards().get(2), board.handleSuggestion(test, jane));

		// All three can disprove it
		test = new Solution(brad.getCards().get(5).getName(), jane.getCards().get(1).getName(),
				john.getCards().get(0).getName());

		// Returns Jane's since she is the first after John
		assertEquals(jane.getCards().get(1), board.handleSuggestion(test, john));

		// Returns Brad's since he is the first after Jane
		// Jane is human, Brad is not, Brad goes first anyway because he is next
		assertEquals(brad.getCards().get(5), board.handleSuggestion(test, jane));

		// Returns John's since he is the first after Brad
		assertEquals(john.getCards().get(0), board.handleSuggestion(test, brad));
	}
}

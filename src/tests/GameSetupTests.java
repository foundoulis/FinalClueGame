package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class GameSetupTests {
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
	}

	@Test
	public void loadThePeople() throws FileNotFoundException {
		List<Player> players = board.getPlayers();

		// Test to make sure first and last players are fully loaded correctly
		// Test that the other players are loaded in general

		// Test some names
		assertEquals("John Doe", players.get(0).getName());
		assertEquals("Jane Doe", players.get(1).getName());
		assertEquals("Brad Pitt", players.get(2).getName());

		// Test some colors
		assertEquals(Color.GREEN, players.get(0).getColor());
		assertEquals(Color.BLUE, players.get(2).getColor());

		// Test some positions
		assertEquals(0, players.get(0).getRow());
		assertEquals(1, players.get(0).getColumn());

		assertEquals(5, players.get(2).getRow());
		assertEquals(20, players.get(2).getColumn());

		// Test people are computers or people
		assertTrue(players.get(0) instanceof HumanPlayer);
		assertTrue(players.get(2) instanceof ComputerPlayer);
	}

	@Test
	public void loadTheDeck() {
		final List<Card> deck = board.getDeck();

		// Test that the deck has the right number of cards
		assertEquals(6 + 6 + 9, deck.size());

		// Test that the deck has the right number of each card type
		int weaponCount = 0;
		int roomCount = 0;
		int peopleCount = 0;
		for (Card card : deck) {
			if (card.getType() == CardType.WEAPON) {
				peopleCount++;
			} else if (card.getType() == CardType.ROOM) {
				roomCount++;
			} else if (card.getType() == CardType.PERSON) {
				weaponCount++;
			}
		}
		assertEquals(6, peopleCount);
		assertEquals(9, roomCount);
		assertEquals(6, weaponCount);

		// Test that the names were loaded right
		boolean foundGun = false;
		boolean foundPlatoon = false;
		boolean foundMasterBedroom = false;
		for (Card card : deck) {
			if (card.getType() == CardType.WEAPON && !foundGun && card.getName().equals("Gun")) {
				foundGun = true;
			} else if (card.getType() == CardType.ROOM && !foundMasterBedroom
					&& card.getName().equals("Master bedroom")) {
				foundMasterBedroom = true;
			} else if (card.getType() == CardType.PERSON && !foundPlatoon && card.getName().equals("Mr. Platoon")) {
				foundPlatoon = true;
			}
		}
		assertTrue(foundGun);
		assertTrue(foundPlatoon);
		assertTrue(foundMasterBedroom);
	}

	@Test
	public void dealTheCards() {
		final List<Player> players = board.getPlayers();
		final List<Card> deck = board.getDeck();

		// Test that all cards have been dealt
		for (Card card : deck) {
			assertTrue(card.hasBeenDealt());
		}

		// Test that all players have roughly the same amount of cards
		int cardsPerPlayer = (deck.size() - 3) / players.size();
		for (Player player : players) {
			assertTrue(player.getCards().size() >= cardsPerPlayer);
		}

		// Test that all players have different cards
		for (int i = 0; i < players.size(); i++) {
			List<Card> p1Cards = players.get(i).getCards();

			for (int j = i + 1; j < players.size(); j++) {
				List<Card> p2Cards = players.get(j).getCards();

				for (Card card : p1Cards) {
					assertFalse(p2Cards.contains(card));
				}
			}
		}
	}
}

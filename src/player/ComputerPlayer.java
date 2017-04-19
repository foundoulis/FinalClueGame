package player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Solution;

import card.Card;
import card.CardType;

public class ComputerPlayer extends Player {
	private BoardCell lastRoom;
	private Solution accusation;
	private boolean makeAccusationOnNextTurn = false;

	public ComputerPlayer(String name, Color color, int row, int col) {
		super(name, color, row, col);
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		return pickLocation(targets, new Random());
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets, Random rand) {
		// Converts the targets set into an array list
		ArrayList<BoardCell> targetList = new ArrayList<BoardCell>(targets);
		Collections.sort(targetList);

		// Loops through each spot in the list returning if it is a valid room
		for (int i = 0; i < targetList.size(); i++) {
			if (targetList.get(i).isRoom() && (lastRoom == null || targetList.get(i).getInitial() != lastRoom.getInitial())) {
				lastRoom = targetList.get(i);
				return targetList.get(i);
			}
		}
		
		// finds a random number between 0 and the target list size - 1
		int value = rand.nextInt(targetList.size());

		// if a valid room was not found, a random target is selected
		return targetList.get(value);
	}

	public void makeAccusation() {
	}

	public Solution createSuggestion() {
		// Get room
		Board board = Board.getInstance();
		BoardCell cell = board.getCellAt(row, getColumn());
		String room = (board.getLegend()).get(cell.getInitial());

		// Sort cards
		List<Card> unseenCards = new ArrayList<Card>(myCards);
		unseenCards.removeAll(seenCards);
		List<Card> weapons = new ArrayList<Card>();
		List<Card> persons = new ArrayList<Card>();
		for (Card card : unseenCards) {
			CardType type = card.getType();
			if (type.equals(CardType.WEAPON)) {
				weapons.add(card);
			} else if (type.equals(CardType.PERSON)) {
				persons.add(card);
			}
		}

		// Get person
		String person = "";
		int personsSize = persons.size();
		if (personsSize == 1) {
			person = persons.get(0).getName();
		} else {
			person = persons.get(ThreadLocalRandom.current().nextInt(0, personsSize)).getName();
		}

		// Get weapon
		String weapon = "";
		int weaponsSize = weapons.size();
		if (weaponsSize == 1) {
			weapon = weapons.get(0).getName();
		} else {
			weapon = weapons.get(ThreadLocalRandom.current().nextInt(0, weaponsSize)).getName();
		}

		return new Solution(person, room, weapon);
	}

	// Used for testing with a fake random
	public Card disproveSuggestion(Solution suggestion, Random random) {
		List<Integer> matchingIndices = new ArrayList<Integer>();
		List<Card> hand = this.getCards();
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getName().equals(suggestion.getPerson())
					|| hand.get(i).getName().equals(suggestion.getRoom())
					|| hand.get(i).getName().equals(suggestion.getWeapon())) {
				matchingIndices.add(i);
			}
		}

		if (matchingIndices.size() == 0) {
			return null;
		} else {
			return hand.get(matchingIndices.get(random.nextInt(matchingIndices.size())));
		}
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		return disproveSuggestion(suggestion, new Random());
	}
	
	public void setLastRoom(BoardCell room) {
		lastRoom = room;
	}
	
	public void setAccusation(Solution accusation) {
		makeAccusationOnNextTurn = true;
		this.accusation = accusation;
	}
	
	public boolean isMakeAccusationOnNextTurn() {
		return makeAccusationOnNextTurn;
	}

	public Solution getAccusation() {
		return accusation;
	}
}
